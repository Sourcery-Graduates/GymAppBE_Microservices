package com.sourcery.gymapp.backend.authentication.integration;

import com.sourcery.gymapp.backend.authentication.config.BaseAuthKafkaIntegrationTest;
import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.exception.ErrorCode;
import com.sourcery.gymapp.backend.authentication.exception.ErrorResponse;
import com.sourcery.gymapp.backend.authentication.exception.FieldResponse;
import com.sourcery.gymapp.backend.authentication.factory.RegistrationRequestFactory;
import com.sourcery.gymapp.backend.authentication.model.User;
import com.sourcery.gymapp.backend.authentication.repository.UserRepository;
import com.sourcery.gymapp.backend.email.service.EmailService;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AuthControllerTest extends BaseAuthKafkaIntegrationTest {

    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    UserRepository userRepository;
    @MockBean
    EmailService emailService;
    @Nested
    @DisplayName("Register endpoint tests")
    public class RegisterTests {

        private WebTestClient.ResponseSpec callPostRegistration(RegistrationRequest request) {
            return webTestClient.post().uri(
                            uriBuilder -> uriBuilder
                                    .path("/api/auth/register")
                                    .build()
                    )
                    .bodyValue(request)
                    .exchange();
        }

        @Nested
        @DisplayName("Validation tests")
        public class ValidationTests {
            @Test
            void registrationWithNotMatchingPasswords_shouldReturnMethodArgumentNotValidException() {
                RegistrationRequest request = RegistrationRequestFactory.createRegistrationRequestDifferentPasswords();

                ErrorResponse responseBody = callPostRegistration(request)
                        .expectStatus()
                        .isBadRequest()
                        .expectBody(ErrorResponse.class)
                        .returnResult().getResponseBody();

                assertThat(responseBody).isNotNull();

                assertThat(responseBody.code()).isEqualTo(ErrorCode.REQUEST_VALIDATION_ERROR);
                assertThat(responseBody.message()).isEqualTo("Request validation error");
                assertThat(responseBody.fields().size()).isEqualTo(1);
                FieldResponse firstField = responseBody.fields().getFirst();
                assertThat(firstField.field()).isEqualTo("confirmPassword");
                assertThat(firstField.error()).isEqualTo("password and confirmPassword do not match");
            }

            @Test
            void registrationWithNullValues_shouldReturnMethodArgumentNotValidException() {
                RegistrationRequest request = RegistrationRequestFactory.createRegistrationRequest(null, null, null, null, null, null, null, null);

                ErrorResponse responseBody = callPostRegistration(request)
                        .expectStatus()
                        .isBadRequest()
                        .expectBody(ErrorResponse.class)
                        .returnResult().getResponseBody();

                assertThat(responseBody).isNotNull();

                assertThat(responseBody.code()).isEqualTo(ErrorCode.REQUEST_VALIDATION_ERROR);
                assertThat(responseBody.message()).isEqualTo("Request validation error");
                assertThat(responseBody.fields().size()).isEqualTo(6);
            }

            @Test
            void registrationWithEmptyStringValues_shouldReturnMethodArgumentNotValidException() {
                RegistrationRequest request = RegistrationRequestFactory.createRegistrationRequest("", "", "", "", "", "", "", "");

                ErrorResponse responseBody = callPostRegistration(request)
                        .expectStatus()
                        .isBadRequest()
                        .expectBody(ErrorResponse.class)
                        .returnResult().getResponseBody();

                assertThat(responseBody).isNotNull();

                assertThat(responseBody.code()).isEqualTo(ErrorCode.REQUEST_VALIDATION_ERROR);
                assertThat(responseBody.message()).isEqualTo("Request validation error");
                assertThat(responseBody.fields().size()).isEqualTo(7);
            }

            @Test
            void registrationWithTooShortPassword_shouldReturnMethodArgumentNotValidException() {
                RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();
                request.setPassword("1234567");
                request.setConfirmPassword("1234567");

                ErrorResponse responseBody = callPostRegistration(request)
                        .expectStatus()
                        .isBadRequest()
                        .expectBody(ErrorResponse.class)
                        .returnResult().getResponseBody();

                assertThat(responseBody).isNotNull();

                assertThat(responseBody.code()).isEqualTo(ErrorCode.REQUEST_VALIDATION_ERROR);
                assertThat(responseBody.message()).isEqualTo("Request validation error");
                assertThat(responseBody.fields().size()).isEqualTo(1);
                FieldResponse firstField = responseBody.fields().getFirst();
                assertThat(firstField.field()).isEqualTo("password");
                assertThat(firstField.error()).isEqualTo("Password must be at least 8 characters long");
            }

            @Test
            void registrationWithNotEmailFormat_shouldReturnMethodArgumentNotValidException() {
                RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();
                request.setEmail("NotFormattedEmail");

                ErrorResponse responseBody = callPostRegistration(request)
                        .expectStatus()
                        .isBadRequest()
                        .expectBody(ErrorResponse.class)
                        .returnResult().getResponseBody();

                assertThat(responseBody).isNotNull();

                assertThat(responseBody.code()).isEqualTo(ErrorCode.REQUEST_VALIDATION_ERROR);
                assertThat(responseBody.message()).isEqualTo("Request validation error");
                assertThat(responseBody.fields().size()).isEqualTo(1);
                FieldResponse firstField = responseBody.fields().getFirst();
                assertThat(firstField.field()).isEqualTo("email");
                assertThat(firstField.error()).isEqualTo("Email should be valid");
            }

            @Test
            void registrationWithValidFields_ShouldReturnOkStatus() {
                RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();

                callPostRegistration(request)
                        .expectStatus()
                        .isOk();
            }
        }

        @Nested
        @DisplayName("Kafka events")
        public class KafkaEvents {

            @Test
            void testProfileIsCreatedInDB() {
                RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();

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
                            assertEquals(request.getUsername(), profile.getUsername());
                            assertEquals(request.getFirstName(), profile.getFirstName());
                            assertEquals(request.getLastName(), profile.getLastName());
                        });
            }

            @Test
            void testEmailIsSent() {
                RegistrationRequest request = RegistrationRequestFactory.createRegistrationValidRequest();

                webTestClient.post().uri("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isOk();


                await()
                        .pollInterval(Duration.ofSeconds(3))
                        .atMost(10, SECONDS)
                        .untilAsserted(() -> verify(emailService, times(1)).sendEmail(any()));

            }
        }
    }
}

