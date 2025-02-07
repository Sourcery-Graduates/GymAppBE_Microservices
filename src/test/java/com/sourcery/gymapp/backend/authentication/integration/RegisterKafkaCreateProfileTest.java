package com.sourcery.gymapp.backend.authentication.integration;

import com.sourcery.gymapp.backend.authentication.controller.AuthController;
import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.producer.AuthKafkaProducer;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import com.sourcery.gymapp.backend.config.integration.BaseKafkaIntegrationTest;
import com.sourcery.gymapp.backend.events.RegistrationEvent;
import com.sourcery.gymapp.backend.userProfile.consumer.UserProfileKafkaConsumer;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.time.Duration;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegisterKafkaCreateProfileTest extends BaseKafkaIntegrationTest {

    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    UserRepository userRepository;

    private RegistrationRequest createRegistrationRequest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("testUser");
        request.setPassword("securePassword");
        request.setConfirmPassword("securePassword");
        request.setEmail("test@example.com");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setBio("Lorem ipsum dolor sit amet");

        return request;
    }

    @Test
    void testProfileIsCreatedInDB() {
        RegistrationRequest request = createRegistrationRequest();
        webTestClient.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        User createdUser = userRepository.findByUsername("testUser").orElse(null);
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
