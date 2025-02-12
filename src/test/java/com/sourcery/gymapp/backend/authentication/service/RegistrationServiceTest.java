package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.exception.UserAlreadyExistsException;
import com.sourcery.gymapp.backend.authentication.mapper.UserMapper;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.producer.AuthKafkaProducer;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthKafkaProducer authKafkaProducer;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        registrationService.register(registrationRequest);

        verify(userRepository, times(1)).save(any());
        verify(authKafkaProducer, times(1)).sendRegistrationEvent(any());
    }

    @Test
    void testRegister_withExistingUser() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("existingUser");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> registrationService.register(registrationRequest));
    }
}
