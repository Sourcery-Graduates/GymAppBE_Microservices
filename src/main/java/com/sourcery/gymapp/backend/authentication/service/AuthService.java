package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.dto.UserAuthDto;
import com.sourcery.gymapp.backend.authentication.dto.UserDetailsDto;
import com.sourcery.gymapp.backend.authentication.event.PasswordResetEvent;
import com.sourcery.gymapp.backend.authentication.exception.*;
import com.sourcery.gymapp.backend.authentication.jwt.GymAppJwtProvider;
import com.sourcery.gymapp.backend.authentication.mapper.UserMapper;
import com.sourcery.gymapp.backend.authentication.model.EmailToken;
import com.sourcery.gymapp.backend.authentication.model.TokenType;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.producer.AuthKafkaProducer;
import com.sourcery.gymapp.backend.authentication.repository.EmailTokenRepository;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import com.sourcery.gymapp.backend.events.EmailSendEvent;
import com.sourcery.gymapp.backend.events.RegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final GymAppJwtProvider jwtProvider;
    private final AuthKafkaProducer kafkaEventsProducer;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher applicationPublisher;
    private final EmailTokenRepository emailTokenRepository;

    @Value("${frontend.base_url}")
    private String applicationURL;

    @Value("${frontend.registration_verification_path}")
    private String registerVerificationEndpoint;

    @Transactional(readOnly = true)
    public UserAuthDto authenticateUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            UserDetailsDto userDetailsDto = (UserDetailsDto) userDetails;
            String token = jwtProvider.generateToken(userDetailsDto.getUsername(), userDetailsDto.getId());
            return userMapper.toAuthDto(userDetailsDto, token);
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            UserDetailsDto userDetails = (UserDetailsDto) userDetailsService.loadUserByUsername(jwt.getClaim("email"));
            String token = jwt.getTokenValue();
            return userMapper.toAuthDto(userDetails, token);
        }
        throw new UserNotAuthenticatedException();
    }

    public void register(RegistrationRequest registrationRequest) {
        User user = transactionTemplate.execute(status -> {
            if (userRepository.existsByEmail(registrationRequest.getEmail())) {
                return null;
            }

            registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            return userRepository.save(userMapper.toEntity(registrationRequest));
        });

        if (user == null) {
            throw new UserAlreadyExistsException();
        }

        RegistrationEvent event = userMapper.toRegistrationEvent(user, registrationRequest);
        kafkaEventsProducer.sendRegistrationEvent(event);

        EmailSendEvent emailSendEvent = createRegistrationEmailEvent(user);
        kafkaEventsProducer.sendRegistrationEmail(emailSendEvent, user.getId());
    }

    @Transactional
    public ResponseEntity<String> registerVerification(String token) {
        EmailToken emailToken = emailTokenRepository.findByToken(token).orElseThrow(RegistrationTokenNotFoundException::new);

        if (emailToken.getUser().isEnabled()) {
            return ResponseEntity.badRequest().body("This account was already verified earlier");
        }

        if (emailToken.getType() != TokenType.REGISTRATION) {
            return ResponseEntity.badRequest().body("Wrong token type was %s, expected %s".formatted(emailToken.getType(), TokenType.REGISTRATION));
        }

        User user = emailToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        emailTokenRepository.delete(emailToken);

        return ResponseEntity.ok("Account verified successfully");
    }

    @Transactional
    public ResponseEntity<String> passwordResetRequest(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Can't find user by email " + email));
        if (!user.isEnabled()) {
            throw new UserAccountNotVerifiedException("Verify your account via email before resetting password!");
        }

        applicationPublisher.publishEvent(new PasswordResetEvent(user, applicationURL));

        return ResponseEntity.ok().body("Email with password reset was send to your email address");
    }

    @Transactional
    public ResponseEntity<String> passwordChange(String password, String token) {
        try {
            EmailToken emailToken = emailTokenRepository.findByTokenAndLockRowAccess(token).orElseThrow(PasswordResetTokenNotFoundException::new);

            if (emailToken.getType() != TokenType.PASSWORD_RECOVERY) {
                return ResponseEntity.badRequest().body("Wrong token type was %s, expected %s".formatted(emailToken.getType(), TokenType.PASSWORD_RECOVERY));
            }

            if (emailToken.getExpirationTime().isBefore(ZonedDateTime.now())) {
                emailTokenRepository.delete(emailToken);
                return ResponseEntity.badRequest().body("Link already expired, please send another password change request");
            }
            User user = emailToken.getUser();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            emailTokenRepository.delete(emailToken);
        } catch (CannotAcquireLockException e) {
            throw new TokenAlreadyInUsageException();
        }


        return ResponseEntity.ok("Password has been changed!");
    }

    private EmailSendEvent createRegistrationEmailEvent(User user) {

        String verificationToken = UUID.randomUUID().toString();

        EmailToken emailToken = new EmailToken();

        emailToken.setType(TokenType.REGISTRATION);
        emailToken.setToken(verificationToken);
        emailToken.setUser(user);

        emailTokenRepository.save(emailToken);

        String verificationURL = applicationURL + registerVerificationEndpoint + verificationToken;

        return createRegisterVerificationEmail(user, verificationURL);
    }

    public EmailSendEvent createRegisterVerificationEmail(User user, String verificationUrl) {
        String subject = "Email verificaton";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, %s </p>".formatted(user.getUsername()) +
                "<p>Thank you for registering with us.</p>" +
                "<p>Please, follow the link below to complete your registration. </p>" +
                "<a href=\"" + verificationUrl + "\">Verify your email to activate your account </a>" +
                "<p> Thank you <br> Users Registration Portal Service</p>" +
                "<p style=\"font-style: italic; text-decoration: underline;\"> This is automated message, please dont reply to it</p>";
        return new EmailSendEvent(subject, senderName, mailContent, user.getEmail());
    }
}
