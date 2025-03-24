package com.sourcery.gymapp.backend.authentication.service;

import com.sourcery.gymapp.backend.authentication.config.OidcDefaults;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.producer.AuthKafkaProducer;
import com.sourcery.gymapp.backend.events.RegistrationEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OidcUserEventServiceTest {

    @InjectMocks
    private OidcUserEventService eventService;

    @Mock
    private AuthKafkaProducer kafkaProducer;

    @Mock
    private User user;

    @Mock
    private OidcUser oidcUser;

    @Mock
    private OidcDefaults oidcDefaults;

    @Captor
    private ArgumentCaptor<RegistrationEvent> eventCaptor;

    private final UUID userId = UUID.randomUUID();

    @Test
    public void sendUserCreationEvents_shouldCreateAndSendEvent() {
        // Arrange
        String username = "testuser";
        String email = "test@example.com";
        String givenName = "Test";
        String familyName = "User";

        when(user.getId()).thenReturn(userId);
        when(user.getUsername()).thenReturn(username);
        when(user.getEmail()).thenReturn(email);
        when(oidcUser.getGivenName()).thenReturn(givenName);
        when(oidcUser.getFamilyName()).thenReturn(familyName);
        when(oidcDefaults.getDefaultLocation()).thenReturn("Planet Earth");
        when(oidcDefaults.getDefaultBio()).thenReturn("Gym App Enthusiast");

        // Act
        eventService.sendUserCreationEvents(user, oidcUser);

        // Assert
        verify(kafkaProducer).sendRegistrationEvent(eventCaptor.capture());

        RegistrationEvent capturedEvent = eventCaptor.getValue();
        assertEquals(userId, capturedEvent.userId());
        assertEquals(username, capturedEvent.username());
        assertEquals(givenName, capturedEvent.firstName());
        assertEquals(familyName, capturedEvent.lastName());
        assertEquals("Planet Earth", capturedEvent.location());
        assertEquals("Gym App Enthusiast", capturedEvent.bio());
    }

    @Test
    public void sendUserCreationEvents_shouldHandleNullNames() {
        // Arrange
        when(user.getId()).thenReturn(userId);
        when(user.getUsername()).thenReturn("testuser");
        when(oidcUser.getGivenName()).thenReturn(null);
        when(oidcUser.getFamilyName()).thenReturn(null);
        when(oidcDefaults.getDefaultGivenName()).thenReturn("Gym");
        when(oidcDefaults.getDefaultFamilyName()).thenReturn("User");
        when(oidcDefaults.getDefaultLocation()).thenReturn("Planet Earth");
        when(oidcDefaults.getDefaultBio()).thenReturn("Gym App Enthusiast");

        // Act
        eventService.sendUserCreationEvents(user, oidcUser);

        // Assert
        verify(kafkaProducer).sendRegistrationEvent(eventCaptor.capture());

        // Проверяем, что были использованы значения по умолчанию
        RegistrationEvent capturedEvent = eventCaptor.getValue();
        assertEquals("Gym", capturedEvent.firstName());
        assertEquals("User", capturedEvent.lastName());
    }
}
