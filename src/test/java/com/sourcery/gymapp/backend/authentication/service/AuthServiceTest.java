package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.dto.UserAuthDto;
import com.sourcery.gymapp.backend.authentication.dto.UserDetailsDto;
import com.sourcery.gymapp.backend.authentication.exception.UserAlreadyExistsException;
import com.sourcery.gymapp.backend.authentication.jwt.GymAppJwtProvider;
import com.sourcery.gymapp.backend.authentication.mapper.UserMapper;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.producer.AuthKafkaProducer;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import com.sourcery.gymapp.backend.authentication.exception.UserNotAuthenticatedException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
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
    private GymAppJwtProvider jwtProvider;

    @Mock
    private AuthKafkaProducer authKafkaProducer;

    @Mock
    private TransactionTemplate transactionTemplate;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_withValidAuthentication() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsDto userDetailsDto = mock(UserDetailsDto.class);
        String token = "jwt-token";

        when(authentication.getPrincipal()).thenReturn(userDetailsDto);
        when(userDetailsDto.getUsername()).thenReturn("testUser");
        when(userDetailsDto.getId()).thenReturn(UUID.randomUUID());
        when(jwtProvider.generateToken(anyString(), any(UUID.class))).thenReturn(token);
        when(userMapper.toAuthDto(userDetailsDto, token)).thenReturn(new UserAuthDto(token, "testUser", null, null));

        UserAuthDto result = authService.authenticateUser(authentication);

        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        assertEquals("testUser", result.username());
    }

    @Test
    void testAuthenticateUser_withInvalidPrincipal() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("invalidPrincipal");

        assertThrows(UserNotAuthenticatedException.class, () -> authService.authenticateUser(authentication));
    }

    @Test
    void testRegister_withNewUser() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUser");
        registrationRequest.setPassword("password123");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setFirstName("test");
        registrationRequest.setLastName("user");

        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());

        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        when(userRepository.save(any())).thenReturn(mockUser);
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> userRepository.save(mockUser));

        authService.register(registrationRequest);

        verify(userRepository, times(1)).save(any());
        verify(authKafkaProducer, times(1)).sendRegistrationEvent(any());
    }

    @Test
    void testRegister_withExistingUser() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("existingUser");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(registrationRequest));
    }
}
