package com.sourcery.gymapp.backend.authentication.controller;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.dto.UserAuthDto;
import com.sourcery.gymapp.backend.authentication.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testAuthenticate() {
        Authentication authentication = mock(Authentication.class);
        UserAuthDto userAuthDto = new UserAuthDto("token123", "testUser", null, null);

        when(authService.authenticateUser(authentication)).thenReturn(userAuthDto);

        UserAuthDto result = authController.authenticate(authentication);

        assertNotNull(result);
        assertEquals("token123", result.token());
        assertEquals("testUser", result.username());
    }

    @Test
    void testRegister() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUser");
        registrationRequest.setPassword("password123");
        registrationRequest.setEmail("test@example.com");

        doNothing().when(authService).register(any(RegistrationRequest.class));

        ResponseEntity<String> response = authController.register(registrationRequest);

        assertEquals(ResponseEntity.ok("User registered successfully"), response);
        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).register(any(RegistrationRequest.class));
    }
}
