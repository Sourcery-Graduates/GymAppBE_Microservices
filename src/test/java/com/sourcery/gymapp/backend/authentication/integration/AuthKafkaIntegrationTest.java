package com.sourcery.gymapp.backend.authentication.integration;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.factory.RegistrationRequestFactory;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import com.sourcery.gymapp.backend.config.integration.BaseKafkaIntegrationTest;
import com.sourcery.gymapp.backend.email.service.EmailService;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class AuthKafkaIntegrationTest extends BaseKafkaIntegrationTest {

    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    UserRepository userRepository;
    @MockBean
    EmailService emailService;

    @Nested
    @DisplayName("Registration endpoint")
    public class RegistrationEndpoint {

//        @Test
//        void testProfileIsCreatedInDB() {
//            RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();
//
//            webTestClient.post().uri("/api/auth/register")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(request)
//                    .exchange()
//                    .expectStatus().isOk();
//
//            User createdUser = userRepository.findByEmail("test@example.com").orElse(null);
//            assertNotNull(createdUser);
//            UUID userId = createdUser.getId();
//
//            await()
//                    .pollInterval(Duration.ofSeconds(3))
//                    .atMost(10, SECONDS)
//                    .untilAsserted(() -> {
//                        UserProfile profile = userProfileRepository.findUserProfileByUserId(userId).orElse(null);
//                        assertNotNull(profile);
//                        assertEquals("testUser", profile.getUsername());
//                        assertEquals("Test", profile.getFirstName());
//                        assertEquals("User", profile.getLastName());
//                    });
//        }

//        @Test
//        void testEmailIsSent() {
//            RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();
//
//            webTestClient.post().uri("/api/auth/register")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(request)
//                    .exchange()
//                    .expectStatus().isOk();
//
//
//            await()
//                    .pollInterval(Duration.ofSeconds(3))
//                    .atMost(10, SECONDS)
//                    .untilAsserted(() -> verify(emailService, times(1)).sendEmail(any()));
//        }
    }
}