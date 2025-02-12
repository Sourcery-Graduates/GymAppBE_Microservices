package com.sourcery.gymapp.backend.workout.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class OffsetDateServiceTest {

    private OffsetDateService offsetDateService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneOffset.UTC);
        offsetDateService = new OffsetDateService(fixedClock);
    }

    @Nested
    @DisplayName("Get Weekly Date Range Offset Tests")
    public class GetWeeklyDateRangeOffsetTests {

        @Test
        void getWeeklyDateRangeOffset_shouldReturnCurrentWeek() {
            List<ZonedDateTime> startAndEndOfTheWeek = offsetDateService.getWeeklyDateRangeOffset(0);

            assertEquals(ZonedDateTime.parse("2024-12-30T00:00:00Z"), startAndEndOfTheWeek.getFirst());
            assertEquals(ZonedDateTime.parse("2025-01-05T23:59:59.999999999Z"), startAndEndOfTheWeek.get(1));
        }

        @Test
        void getWeeklyDateRangeOffset_shouldReturnPreviousWeek() {
            List<ZonedDateTime> startAndEndOfTheWeek = offsetDateService.getWeeklyDateRangeOffset(1);

            assertEquals(ZonedDateTime.parse("2024-12-23T00:00:00Z"), startAndEndOfTheWeek.getFirst());
            assertEquals(ZonedDateTime.parse("2024-12-29T23:59:59.999999999Z"), startAndEndOfTheWeek.get(1));
        }

        @Test
        void getWeeklyDateRangeOffset_shouldThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> offsetDateService.getWeeklyDateRangeOffset(-1));
        }
    }

    @Nested
    @DisplayName("Get Offset Start And Current Date Tests")
    public class getOffsetStartAndCurrentDateTests {

        @Test
        void getOffsetStartAndCurrentDate_shouldReturnLastThreeMonthsFromCurrentDate() {
            List<ZonedDateTime> startAndEndOfTheMonth = offsetDateService.getOffsetStartAndCurrentDate(3);

            assertEquals(ZonedDateTime.parse("2024-10-01T00:00:00Z"), startAndEndOfTheMonth.getFirst());
            assertEquals(ZonedDateTime.parse("2025-01-01T00:00:00Z"), startAndEndOfTheMonth.get(1));
        }

        @Test
        void getOffsetStartAndCurrentDate_shouldReturnLastTwoMonthsFromCurrentDate() {
            List<ZonedDateTime> startAndEndOfTheMonth = offsetDateService.getOffsetStartAndCurrentDate(2);

            assertEquals(ZonedDateTime.parse("2024-11-01T00:00:00Z"), startAndEndOfTheMonth.getFirst());
            assertEquals(ZonedDateTime.parse("2025-01-01T00:00:00Z"), startAndEndOfTheMonth.get(1));
        }

        @Test
        void getOffsetStartAndCurrentDate_shouldThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> offsetDateService.getOffsetStartAndCurrentDate(-1));
        }
    }

    @Nested
    @DisplayName("Get Monthly Date Range Offset Tests")
    public class GetMonthlyDateRangeOffsetTests {

        @Test
        void getMonthlyDateRangeOffset_shouldReturnMonthFromThreeMonthsAgo() {
            List<ZonedDateTime> startAndEndOfTheMonth = offsetDateService.getMonthlyDateRangeOffset(3);

            assertEquals(ZonedDateTime.parse("2024-10-01T00:00:00Z"), startAndEndOfTheMonth.getFirst());
            assertEquals(ZonedDateTime.parse("2024-10-31T23:59:59.999999999Z"), startAndEndOfTheMonth.get(1));
        }

        @Test
        void getMonthlyDateRangeOffset_shouldReturnMonthFromTwoMonthsAgo() {
            List<ZonedDateTime> startAndEndOfTheMonth = offsetDateService.getMonthlyDateRangeOffset(2);

            assertEquals(ZonedDateTime.parse("2024-11-01T00:00:00Z"), startAndEndOfTheMonth.getFirst());
            assertEquals(ZonedDateTime.parse("2024-11-30T23:59:59.999999999Z"), startAndEndOfTheMonth.get(1));
        }

        @Test
        void getMonthlyDateRangeOffset_shouldThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> offsetDateService.getMonthlyDateRangeOffset(-1));
        }
    }
}