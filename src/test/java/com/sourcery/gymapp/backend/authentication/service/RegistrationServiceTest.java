package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.exception.*;
import com.sourcery.gymapp.backend.authentication.mapper.UserMapper;
import com.sourcery.gymapp.backend.authentication.model.EmailToken;
import com.sourcery.gymapp.backend.authentication.model.TokenType;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.repository.EmailTokenRepository;
import com.sourcery.gymapp.backend.authentication.producer.AuthKafkaProducer;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthKafkaProducer authKafkaProducer;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private ApplicationEventPublisher applicationPublisher;

    @Mock
    private EmailTokenRepository emailTokenRepository;

    @InjectMocks
    private AuthService authService;

    private final String userName = "testUser";

    private final String userEmail = "test@example.com";

    private final String userPassword = "password123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Nested
    @DisplayName("Registration tests")
    public class RegistrationTests{
        @Test
        void testRegister_withNewUser() {
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.setUsername(userName);
            registrationRequest.setPassword(userPassword);
            registrationRequest.setEmail(userEmail);
            registrationRequest.setFirstName("test");
            registrationRequest.setLastName("user");

            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername(userName);
            user.setEmail(userEmail);
            user.setPassword(userPassword);

            when(userRepository.existsByEmail(userEmail)).thenReturn(false);
            when(passwordEncoder.encode(userPassword)).thenReturn("encodedPassword");
            when(userMapper.toEntity(registrationRequest)).thenReturn(user);
            when(userRepository.save(any())).thenReturn(user);
            when(transactionTemplate.execute(any())).thenAnswer(invocation -> userRepository.save(user));

            authService.register(registrationRequest);

            verify(userRepository, times(1)).save(any());
            verify(authKafkaProducer, times(1)).sendRegistrationEvent(any());
    }

        @Test
        void testRegister_withExistingUser() {
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.setEmail(userEmail);

            when(userRepository.existsByEmail(userEmail)).thenReturn(true);

            assertThrows(UserAlreadyExistsException.class, () -> authService.register(registrationRequest));
        }
    }

    @Nested
    @DisplayName("Registration verification tests")
    public class RegisterVerification {
        @Test
        void testRegisterVerification_noTokenFound () {

            when(emailTokenRepository.findByToken(any())).thenReturn(Optional.empty());

            assertThrows(RegistrationTokenNotFoundException.class, () -> {
                authService.registerVerification(any());
            });
        }

        @Test
        void testRegisterVerification_userAlreadyVerified () {
            EmailToken emailToken = new EmailToken();
            User user = new User();
            user.setEnabled(true);
            emailToken.setUser(user);

            when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(emailToken));

            ResponseEntity<String> response = authService.registerVerification(any());

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("This account was already verified earlier", response.getBody());
        }

        @Test
        void testRegisterVerification_notRegisterVerificationTokenType () {
            EmailToken emailToken = new EmailToken();
            User user = new User();
            user.setEnabled(false);
            emailToken.setUser(user);
            emailToken.setType(TokenType.PASSWORD_RECOVERY);

            when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(emailToken));

            ResponseEntity<String> response = authService.registerVerification(any());

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Wrong token type was %s, expected %s".formatted(emailToken.getType(), TokenType.REGISTRATION), response.getBody());
        }

        @Test
        void testRegisterVerification_withValidUserAndToken () {
            EmailToken emailToken = new EmailToken();
            User user = new User();
            user.setEnabled(false);
            emailToken.setUser(user);
            emailToken.setType(TokenType.REGISTRATION);

            when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(emailToken));

            ResponseEntity<String> response = authService.registerVerification(any());

            verify(userRepository, times(1)).save(any());
            verify(emailTokenRepository, times(1)).delete(any());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Account verified successfully", response.getBody());
        }
    }

    @Nested
    @DisplayName("Password reset / change")
    public class PasswordReset {
        @Nested
        @DisplayName("Password reset request")
        public class PasswordResetRequest {

            @Test
            void passwordResetRequest_withInvalidEmail() {
                when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

                assertThrows(UsernameNotFoundException.class, () ->
                authService.passwordResetRequest(anyString()));
            }

            @Test
            void passwordResetRequest_withUserDisabled() {
                User user = new User();
                user.setEnabled(false);

                when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

                assertThrows(UserAccountNotVerifiedException.class, () -> authService.passwordResetRequest(anyString()));
            }

            @Test
            void passwordResetRequest_withValidUser() {
                User user = new User();
                user.setEnabled(true);

                when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
                doNothing().when(applicationPublisher).publishEvent(any(PasswordResetEvent.class));

                ResponseEntity<String> response = authService.passwordResetRequest(anyString());

                verify(applicationPublisher, times(1)).publishEvent(any(PasswordResetEvent.class));
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("Email with password reset was send to your email address", response.getBody());
            }
        }
        @Nested
        @DisplayName("Password change")
        public class PasswordChange {
            String passwordParam = "password";
            String tokenParam = "token";

            @Test
            void passwordChange_withNoToken() {

                when(emailTokenRepository.findByTokenAndLockRowAccess(tokenParam)).thenReturn(Optional.empty());

                assertThrows(PasswordResetTokenNotFoundException.class, () -> authService.passwordChange(passwordParam, tokenParam));
            }

            @Test
            void passwordChange_withWrongTokenType() {
                EmailToken emailToken = new EmailToken();
                emailToken.setType(TokenType.REGISTRATION);

                when(emailTokenRepository.findByTokenAndLockRowAccess(tokenParam)).thenReturn(Optional.of(emailToken));

                ResponseEntity<String> response = authService.passwordChange(passwordParam, tokenParam);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Wrong token type was %s, expected %s".formatted(emailToken.getType(), TokenType.PASSWORD_RECOVERY), response.getBody());
            }

            @Test
            void passwordChange_withTokenExpired() {
                EmailToken emailToken = new EmailToken();
                emailToken.setType(TokenType.PASSWORD_RECOVERY);
                emailToken.setExpirationTime(ZonedDateTime.now().minusMinutes(15));

                when(emailTokenRepository.findByTokenAndLockRowAccess(tokenParam)).thenReturn(Optional.of(emailToken));
                doNothing().when(emailTokenRepository).delete(any());

                ResponseEntity<String> response = authService.passwordChange(passwordParam, tokenParam);

                verify(emailTokenRepository, times(1)).delete(any());
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Link already expired, please send another password change request", response.getBody());
            }

            @Test
            void passwordChange_withValidInformation() {
                User user = new User();
                EmailToken emailToken = new EmailToken();
                emailToken.setType(TokenType.PASSWORD_RECOVERY);
                emailToken.setExpirationTime(ZonedDateTime.now().plusHours(24));
                emailToken.setUser(user);


                when(emailTokenRepository.findByTokenAndLockRowAccess(tokenParam)).thenReturn(Optional.of(emailToken));
                when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
                when(userRepository.save(any())).thenReturn(user);
                doNothing().when(emailTokenRepository).delete(any());

                ResponseEntity<String> response = authService.passwordChange(passwordParam, tokenParam);

                verify(passwordEncoder, times(1)).encode(anyString());
                verify(userRepository, times(1)).save(any());
                verify(emailTokenRepository, times(1)).delete(any());
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("Password has been changed!", response.getBody());
            }
        }
    }
}
