package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.dto.UserAuthDto;
import com.sourcery.gymapp.backend.authentication.dto.UserDetailsDto;
import com.sourcery.gymapp.backend.authentication.event.RegistrationCompleteEvent;
import com.sourcery.gymapp.backend.authentication.exception.RegistrationTokenNotFoundException;
import com.sourcery.gymapp.backend.authentication.exception.UserAlreadyExistsException;
import com.sourcery.gymapp.backend.authentication.jwt.GymAppJwtProvider;
import com.sourcery.gymapp.backend.authentication.mapper.UserMapper;
import com.sourcery.gymapp.backend.authentication.model.TokenType;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.repository.EmailTokenRepository;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import com.sourcery.gymapp.backend.authentication.exception.UserNotAuthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sourcery.gymapp.backend.authentication.model.EmailToken;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final GymAppJwtProvider jwtProvider;
    private final ApplicationEventPublisher registrationPublisher;
    private final EmailTokenRepository emailTokenRepository;

    @Value("${frontend.base_url}")
    private String applicationURL;

    @Transactional(readOnly = true)
    public UserAuthDto authenticateUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            UserDetailsDto userDetailsDto = (UserDetailsDto) userDetails;
            String token = jwtProvider.generateToken(userDetailsDto.getUsername(),
                    userDetailsDto.getId());
            return userMapper.toAuthDto(userDetailsDto, token);
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            UserDetailsDto userDetails =
                    (UserDetailsDto) userDetailsService.loadUserByUsername(jwt.getClaim("email"));
            String token = jwt.getTokenValue();
            return userMapper.toAuthDto(userDetails, token);
        }
        throw new UserNotAuthenticatedException();
    }

    @Transactional
    public void register(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException();
        }
        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        User user = userMapper.toEntity(registrationRequest);

        userRepository.save(user);

        registrationPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationURL));
    }

    @Transactional
    public ResponseEntity<String> registerVerification(String token) {
        EmailToken emailToken =  emailTokenRepository.findByToken(token)
                .orElseThrow(RegistrationTokenNotFoundException::new);

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
}
