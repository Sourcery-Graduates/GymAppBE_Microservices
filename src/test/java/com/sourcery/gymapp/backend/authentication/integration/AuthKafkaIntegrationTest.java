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

        @Test
        void testProfileIsCreatedInDBAndEmailIsSent() {
            RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();
            String testUsername = UUID.randomUUID().toString()
                    .replaceAll("[^a-zA-Z]", "").substring(0, 8);
            request.setUsername(testUsername);

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
                        System.out.println("All profiles in DB: " + userProfileRepository.findAll());
                        UserProfile profile = userProfileRepository.findUserProfileByUserId(userId).orElse(null);
                        assertNotNull(profile);
                        assertEquals(testUsername, profile.getUsername());
                        assertEquals("Test", profile.getFirstName());
                        assertEquals("User", profile.getLastName());
                        verify(emailService, times(1)).sendEmail(any());
                    });
        }

        @Test
        void testEmailIsSent() {
            RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();
            String testUsername = UUID.randomUUID().toString()
                    .replaceAll("[^a-zA-Z]", "").substring(0, 8);
            request.setUsername(testUsername);

            // get sout message from mocked Email Service that returns sout instead of email sending logic.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            System.setOut(printStream);

            webTestClient.post().uri("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk();


            await()
                    .pollInterval(Duration.ofSeconds(3))
                    .atMost(10, SECONDS)
                    .untilAsserted(() -> verify(emailService, times(1)).sendEmail(any()));

            // Clean up the System.out redirection after the test
            System.setOut(System.out);
        }
    }
}
