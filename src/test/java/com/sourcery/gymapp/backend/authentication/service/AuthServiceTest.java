package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.dto.UserAuthDto;
import com.sourcery.gymapp.backend.authentication.dto.UserDetailsDto;
import com.sourcery.gymapp.backend.authentication.event.RegistrationCompleteEvent;
import com.sourcery.gymapp.backend.authentication.exception.RegistrationTokenNotFoundException;
import com.sourcery.gymapp.backend.authentication.exception.UserAlreadyExistsException;
import com.sourcery.gymapp.backend.authentication.jwt.GymAppJwtProvider;
import com.sourcery.gymapp.backend.authentication.mapper.UserMapper;
import com.sourcery.gymapp.backend.authentication.model.EmailToken;
import com.sourcery.gymapp.backend.authentication.model.TokenType;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.repository.EmailTokenRepository;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import com.sourcery.gymapp.backend.authentication.exception.UserNotAuthenticatedException;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private GymAppJwtProvider jwtProvider;

    @Mock
    private ApplicationEventPublisher emailPublisher;

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
    @DisplayName("Authentication tests")
    public class AuthenticationTests{
        @Test
        void testAuthenticateUser_withValidAuthentication() {
            Authentication authentication = mock(Authentication.class);
            UserDetailsDto userDetailsDto = mock(UserDetailsDto.class);
            String token = "jwt-token";

            when(authentication.getPrincipal()).thenReturn(userDetailsDto);
            when(userDetailsDto.getUsername()).thenReturn(userName);
            when(userDetailsDto.getId()).thenReturn(UUID.randomUUID());
            when(jwtProvider.generateToken(anyString(), any(UUID.class))).thenReturn(token);
            when(userMapper.toAuthDto(userDetailsDto, token)).thenReturn(new UserAuthDto(token, userName, userEmail, null, null));

            UserAuthDto result = authService.authenticateUser(authentication);

            assertNotNull(result);
            assertEquals("jwt-token", result.token());
            assertEquals(userName, result.username());
            assertEquals(userEmail, result.email());
        }

        @Test
        void testAuthenticateUser_withInvalidPrincipal() {
            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn("invalidPrincipal");

            assertThrows(UserNotAuthenticatedException.class, () -> authService.authenticateUser(authentication));
        }
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

            User user = new User();
            user.setUsername(userName);
            user.setEmail(userEmail);
            user.setPassword(userPassword);

            when(userRepository.existsByEmail(userEmail)).thenReturn(false);
            when(passwordEncoder.encode(userPassword)).thenReturn("encodedPassword");
            when(userMapper.toEntity(registrationRequest)).thenReturn(user);

            doNothing().when(emailPublisher).publishEvent(any(RegistrationCompleteEvent.class));
            when(userRepository.save(any())).thenReturn(any());

            authService.register(registrationRequest);

            verify(userRepository, times(1)).save(any());
            verify(emailPublisher, times(1)).publishEvent(any(RegistrationCompleteEvent.class));
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
}
