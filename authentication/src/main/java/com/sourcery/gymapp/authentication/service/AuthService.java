package com.sourcery.gymapp.authentication.service;

import com.sourcery.gymapp.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.authentication.exception.*;
import com.sourcery.gymapp.authentication.mapper.EmailTemplateMapper;
import com.sourcery.gymapp.authentication.mapper.UserMapper;
import com.sourcery.gymapp.authentication.model.EmailToken;
import com.sourcery.gymapp.authentication.model.TokenType;
import com.sourcery.gymapp.authentication.model.User;
import com.sourcery.gymapp.authentication.producer.AuthKafkaProducer;
import com.sourcery.gymapp.authentication.repository.EmailTokenRepository;
import com.sourcery.gymapp.authentication.repository.UserRepository;
import com.sourcery.gymapp.authentication.event.EmailSendEvent;
import com.sourcery.gymapp.authentication.event.RegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthKafkaProducer kafkaEventsProducer;
    private final TransactionTemplate transactionTemplate;
    private final EmailTemplateMapper emailTemplateMapper;
    private final EmailTokenRepository emailTokenRepository;

    @Value("${frontend.base_url}")
    private String applicationURL;

    @Value("${frontend.registration_verification_path}")
    private String registerVerificationEndpoint;

    @Value("${frontend.password_reset_path}")
    private String passwordResetEndpoint;

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
        kafkaEventsProducer.sendEmailEvent(emailSendEvent, user.getId());
    }

    private EmailSendEvent createRegistrationEmailEvent(User user) {
        String verificationToken = UUID.randomUUID().toString();
        EmailToken emailToken = new EmailToken();

        emailToken.setType(TokenType.REGISTRATION);
        emailToken.setToken(verificationToken);
        emailToken.setUser(user);
        emailTokenRepository.save(emailToken);

        String verificationURL = applicationURL + registerVerificationEndpoint + verificationToken;
        return emailTemplateMapper.toRegisterVerificationEmail(user, verificationURL);
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

        EmailSendEvent emailSendEvent = createPasswordResetEvent(user);
        kafkaEventsProducer.sendEmailEvent(emailSendEvent, user.getId());

        return ResponseEntity.ok().body("Email with password reset was send to your email address");
    }

    private EmailSendEvent createPasswordResetEvent(User user) {
        List<EmailToken> emailTokens = emailTokenRepository.findAllByUserIdAndType(user.getId(), TokenType.PASSWORD_RECOVERY);
        if (!emailTokens.isEmpty()) {
            emailTokenRepository.deleteAll(emailTokens);
        }

        String verificationToken = UUID.randomUUID().toString();
        EmailToken emailToken = new EmailToken();
        emailToken.setType(TokenType.PASSWORD_RECOVERY);
        emailToken.setExpirationTime(ZonedDateTime.now().plusHours(24));
        emailToken.setToken(verificationToken);
        emailToken.setUser(user);
        emailTokenRepository.save(emailToken);

        String resetPasswordURL = applicationURL + passwordResetEndpoint + verificationToken;
        return emailTemplateMapper.toPasswordResetEmail(user, resetPasswordURL);
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
}
