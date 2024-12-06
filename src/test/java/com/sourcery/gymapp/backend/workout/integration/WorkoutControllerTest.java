package com.sourcery.gymapp.backend.workout.integration;

import com.sourcery.gymapp.backend.workout.exception.ErrorCode;
import com.sourcery.gymapp.backend.workout.exception.ErrorResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WorkoutControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Nested
    @DisplayName("getWorkoutGridByDate endpoint")
    public class getWorkoutGridByDate {

        ZonedDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0).atZone(ZoneId.of("UTC"));
        ZonedDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59).atZone(ZoneId.of("UTC"));

        @BeforeEach
        void setUpWorkouts() {

        }

        @Test
        void givenInvalidStartDateFormat_shouldReturnMethodArgumentTypeMismatchException() {
            ErrorResponse responseBody = callGetWorkoutGridByDate("123", endDate.toString())
                    .expectStatus().isBadRequest()
                    .expectBody(ErrorResponse.class)
                    .returnResult().getResponseBody();;

            assertThat(responseBody).isNotNull();

            assertThat(responseBody.code()).isEqualTo(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH);
            assertThat(responseBody.message()).contains("startDate");
        }

        @Test
        void givenInvalidEndDateFormat_shouldReturnMethodArgumentTypeMismatchException() {
            ErrorResponse responseBody = callGetWorkoutGridByDate(startDate.toString(), "123")
                    .expectStatus().isBadRequest()
                    .expectBody(ErrorResponse.class)
                    .returnResult().getResponseBody();

            assertThat(responseBody).isNotNull();

            assertThat(responseBody.code()).isEqualTo(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH);
            assertThat(responseBody.message()).contains("endDate");
        }


        @Test
        void givenValidDateSlot_shouldReturnWorkoutGrid() {

        }

        @Test
        void givenValidDateSlot_shouldReturnWorkoutGridGroupedByDate() {

        }

        @Test
        void givenInvalidDateSlot_shouldReturnEmptyWorkoutGrid(){}
    }

    private WebTestClient.ResponseSpec callGetWorkoutGridByDate(String startDate, String endDate) {
        return webTestClient.get().uri(
                uriBuilder -> uriBuilder
                        .path("/api/workout/workout/date")
                        .queryParam(startDate)
                        .queryParam(endDate)
                        .build()
                )
                .exchange();
    }
}
