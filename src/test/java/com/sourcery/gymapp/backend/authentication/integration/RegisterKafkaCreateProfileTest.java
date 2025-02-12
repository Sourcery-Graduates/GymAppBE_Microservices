package com.sourcery.gymapp.backend.authentication.integration;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.event.RegistrationCompleteEvent;
import com.sourcery.gymapp.backend.authentication.factory.RegistrationRequestFactory;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import com.sourcery.gymapp.backend.config.integration.BaseKafkaIntegrationTest;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

import java.time.Duration;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;


public class RegisterKafkaCreateProfileTest extends BaseKafkaIntegrationTest {

    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    UserRepository userRepository;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    private RegistrationCompleteEvent registrationCompleteEvent;

    @BeforeEach
    void setUp() {
        // Set up your mock behavior in @BeforeEach to ensure it runs before every test
        doNothing().when(applicationEventPublisher).publishEvent(registrationCompleteEvent);
    }

    @Test
    void testProfileIsCreatedInDB() {
        RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();

        System.out.println(Mockito.mockingDetails(applicationEventPublisher).isMock() + "XDD");

        assertTrue(Mockito.mockingDetails(applicationEventPublisher).isMock(), "applicationEventPublisher is not a mock!");

        doNothing().when(applicationEventPublisher).publishEvent(registrationCompleteEvent);

        webTestClient.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        User createdUser = userRepository.findByEmail("test@example.com").orElse(null);
        assertNotNull(createdUser);
        UUID userId = createdUser.getId();

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    UserProfile profile = userProfileRepository.findUserProfileByUserId(userId).orElse(null);
                    assertNotNull(profile);
                    assertEquals("testUser", profile.getUsername());
                    assertEquals("Test", profile.getFirstName());
                    assertEquals("User", profile.getLastName());
                });
    }
}
