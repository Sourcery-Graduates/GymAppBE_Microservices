package com.sourcery.gymapp.backend.authentication.controller;

import com.sourcery.gymapp.backend.authentication.config.BaseAuthenticationIntegrationTest;
import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.exception.ErrorCode;
import com.sourcery.gymapp.backend.authentication.exception.ErrorResponse;
import com.sourcery.gymapp.backend.authentication.exception.FieldResponse;
import com.sourcery.gymapp.backend.authentication.factory.RegistrationRequestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthControllerTest extends BaseAuthenticationIntegrationTest {
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
    }
}

