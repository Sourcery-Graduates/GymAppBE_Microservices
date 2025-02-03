package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.*;
import com.sourcery.gymapp.backend.workout.factory.RoutineFactory;
import com.sourcery.gymapp.backend.workout.factory.WorkoutFactory;
import com.sourcery.gymapp.backend.workout.factory.WorkoutStatsFactory;
import com.sourcery.gymapp.backend.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutStatsServiceTest {

    @Mock
    private WorkoutCurrentUserService workoutCurrentUserService;

    @Mock
    private OffsetDateService offsetDateService;

    @Mock
    private WorkoutService workoutService;

    @Mock
    private RoutineService routineService;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private RoutineMapper routineMapper;

    @InjectMocks
    private WorkoutStatsService workoutStatsService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.fromString("910cb97b-d601-4c02-b4b6-c9f985e51a1f");
        lenient().when(workoutCurrentUserService.getCurrentUserId()).thenReturn(userId);
    }

    @Nested
    @DisplayName("Get Workout Stats By Month Tests")
    public class GetWorkoutStatsByMonthTests {
        List<ZonedDateTime> currentMonthRange;
        List<ZonedDateTime> previousMonthRange;

        @BeforeEach
        void setUp() {
            currentMonthRange = List.of(
                    ZonedDateTime.parse("2025-01-01T00:00:00Z"),
                    ZonedDateTime.parse("2025-01-31T23:59:59Z")
            );
            previousMonthRange = List.of(
                    ZonedDateTime.parse("2025-12-01T00:00:00Z"),
                    ZonedDateTime.parse("2025-12-31T23:59:59Z")
            );
        }

        @Test
        void shouldGetWorkoutStatsInFiveMessages_WhenCurrentMonthStatsBetterThanPrevious() {
            when(offsetDateService.getMonthlyDateRangeOffset(0)).thenReturn(currentMonthRange);
            when(offsetDateService.getMonthlyDateRangeOffset(1)).thenReturn(previousMonthRange);

            when(workoutRepository.countWorkoutsByUserIdAndDateBetween(userId, currentMonthRange.get(0), currentMonthRange.get(1)))
                    .thenReturn(10);
            when(workoutRepository.countWorkoutsByUserIdAndDateBetween(userId, previousMonthRange.get(0), previousMonthRange.get(1)))
                    .thenReturn(5);
            when(workoutRepository.getTotalWeightByUserIdAndDateBetween(userId, currentMonthRange.get(0), currentMonthRange.get(1)))
                    .thenReturn(Optional.of(100000));
            when(workoutRepository.getTotalWeightByUserIdAndDateBetween(userId, previousMonthRange.get(0), previousMonthRange.get(1)))
                    .thenReturn(Optional.of(50000));

            List<WorkoutStatsDto> workoutStats = workoutStatsService.getWorkoutStatsByMonth();

            List<String> expectedMessages = List.of(
                    "You have completed 10 workouts this month!",
                    "You have completed 5 more workouts than the last month!",
                    "You have lifted a total of 100000 kg this month!",
                    "100000 kg is like a fully loaded Boeing 747! ✈️",
                    "You have lifted 50000 kg more than the last month!"
            );

            assertEquals(5, workoutStats.size());
            assertAll("Checking all expected messages are present",
                    expectedMessages.stream().map(expectedMessage ->
                            () -> assertTrue(workoutStats.stream()
                                            .anyMatch(dto -> dto.content().equals(expectedMessage)),
                                    "Expected message should be present: " + expectedMessage))
            );
        }

        @Test
        void shouldGetWorkoutStatsInFiveMessagesWithoutPlural_WhenCurrentMonthStatsBetterThanPrevious() {
            when(offsetDateService.getMonthlyDateRangeOffset(0)).thenReturn(currentMonthRange);
            when(offsetDateService.getMonthlyDateRangeOffset(1)).thenReturn(previousMonthRange);

            when(workoutRepository.countWorkoutsByUserIdAndDateBetween(userId, currentMonthRange.get(0), currentMonthRange.get(1)))
                    .thenReturn(1);
            when(workoutRepository.countWorkoutsByUserIdAndDateBetween(userId, previousMonthRange.get(0), previousMonthRange.get(1)))
                    .thenReturn(0);
            when(workoutRepository.getTotalWeightByUserIdAndDateBetween(userId, currentMonthRange.get(0), currentMonthRange.get(1)))
                    .thenReturn(Optional.of(10000));
            when(workoutRepository.getTotalWeightByUserIdAndDateBetween(userId, previousMonthRange.get(0), previousMonthRange.get(1)))
                    .thenReturn(Optional.of(0));

            List<WorkoutStatsDto> workoutStats = workoutStatsService.getWorkoutStatsByMonth();

            List<String> expectedMessages = List.of(
                    "You have completed 1 workout this month!",
                    "You have completed 1 more workout than the last month!",
                    "You have lifted a total of 10000 kg this month!",
                    "10000 kg is like lifting a double-decker bus! 🚌",
                    "You have lifted 10000 kg more than the last month!"
            );

            assertEquals(5, workoutStats.size());
            assertAll("Checking all expected messages are present",
                    expectedMessages.stream().map(expectedMessage ->
                            () -> assertTrue(workoutStats.stream()
                                            .anyMatch(dto -> dto.content().equals(expectedMessage)),
                                    "Expected message should be present: " + expectedMessage))
            );
        }

        @Test
        void shouldGetWorkoutStatsInThreeMessages_WhenPreviousMonthStatsBetter() {
            when(offsetDateService.getMonthlyDateRangeOffset(0)).thenReturn(currentMonthRange);
            when(offsetDateService.getMonthlyDateRangeOffset(1)).thenReturn(previousMonthRange);

            when(workoutRepository.countWorkoutsByUserIdAndDateBetween(userId, currentMonthRange.get(0), currentMonthRange.get(1)))
                    .thenReturn(3);
            when(workoutRepository.countWorkoutsByUserIdAndDateBetween(userId, previousMonthRange.get(0), previousMonthRange.get(1)))
                    .thenReturn(5);
            when(workoutRepository.getTotalWeightByUserIdAndDateBetween(userId, currentMonthRange.get(0), currentMonthRange.get(1)))
                    .thenReturn(Optional.of(50000));
            when(workoutRepository.getTotalWeightByUserIdAndDateBetween(userId, previousMonthRange.get(0), previousMonthRange.get(1)))
                    .thenReturn(Optional.of(100000));

            List<WorkoutStatsDto> workoutStats = workoutStatsService.getWorkoutStatsByMonth();

            List<String> expectedMessages = List.of(
                    "You have completed 3 workouts this month!",
                    "You have lifted a total of 50000 kg this month!",
                    "50000 kg is the weight of a space shuttle ready for launch! 🚀"
            );

            assertEquals(3, workoutStats.size());
            assertAll("Checking all expected messages are present",
                    expectedMessages.stream().map(expectedMessage ->
                            () -> assertTrue(workoutStats.stream()
                                            .anyMatch(dto -> dto.content().equals(expectedMessage)),
                                    "Expected message should be present: " + expectedMessage))
            );
        }

        @Test
        void shouldGetEmptyWorkoutStats_whenNoWorkoutsCurrentMonth() {
            when(offsetDateService.getMonthlyDateRangeOffset(0)).thenReturn(currentMonthRange);
            when(offsetDateService.getMonthlyDateRangeOffset(1)).thenReturn(previousMonthRange);

            when(workoutRepository.countWorkoutsByUserIdAndDateBetween(userId, currentMonthRange.get(0), currentMonthRange.get(1)))
                    .thenReturn(0);
            when(workoutRepository.countWorkoutsByUserIdAndDateBetween(userId, previousMonthRange.get(0), previousMonthRange.get(1)))
                    .thenReturn(12);
            when(workoutRepository.getTotalWeightByUserIdAndDateBetween(userId, currentMonthRange.get(0), currentMonthRange.get(1)))
                    .thenReturn(Optional.of(0));
            when(workoutRepository.getTotalWeightByUserIdAndDateBetween(userId, previousMonthRange.get(0), previousMonthRange.get(1)))
                    .thenReturn(Optional.of(70000));

            List<WorkoutStatsDto> workoutStats = workoutStatsService.getWorkoutStatsByMonth();

            assertEquals(0, workoutStats.size());
        }
    }

    @Nested
    @DisplayName("Get Most Used Routines By Month Tests")
    public class getMostUsedRoutinesByMonthTests {
        List<Routine> routines;
        List<ZonedDateTime> offsetStartMonthRange;

        @BeforeEach
        void setUp() {
            routines = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                routines.add(RoutineFactory.createRoutine());
            }

            offsetStartMonthRange = List.of(
                    ZonedDateTime.parse("2025-10-01T00:00:00Z"),
                    ZonedDateTime.parse("2025-01-31T23:59:59Z")
            );
        }

        @Test
        void shouldGetTopSevenMostUsedRoutines() {
            when(offsetDateService.getOffsetStartAndCurrentDate(3)).thenReturn(offsetStartMonthRange);

            when(workoutRepository.getMostUsedRoutinesByUserIdAndDateBetween(userId, offsetStartMonthRange.get(0), offsetStartMonthRange.get(1)))
                    .thenReturn(routines);

            List<ResponseRoutineSimpleDto> mostUsedRoutines = workoutStatsService.getMostUsedRoutinesByMonth(7, 3);

            assertEquals(7, mostUsedRoutines.size());
            verify(routineMapper, times(7)).toSimpleDto(any(Routine.class));
        }

        @Test
        void shouldGetTopSevenMostUsedRoutines_WhenRoutinesLimitIsNull() {
            when(offsetDateService.getOffsetStartAndCurrentDate(3)).thenReturn(offsetStartMonthRange);

            when(workoutRepository.getMostUsedRoutinesByUserIdAndDateBetween(userId, offsetStartMonthRange.get(0), offsetStartMonthRange.get(1)))
                    .thenReturn(routines);

            List<ResponseRoutineSimpleDto> mostUsedRoutines = workoutStatsService.getMostUsedRoutinesByMonth(null, 3);

            assertEquals(7, mostUsedRoutines.size());
            verify(routineMapper, times(7)).toSimpleDto(any(Routine.class));
        }

        @Test
        void shouldThrowIllegalArgumentException_WhenRoutinesLimitIsNegative() {
            assertThrows(IllegalArgumentException.class, () -> workoutStatsService
                    .getMostUsedRoutinesByMonth(-10, 3), "Routines limit must be greater than 0");
        }

        @Test
        void shouldGetTopTenMostUsedRoutines() {
            when(offsetDateService.getOffsetStartAndCurrentDate(3)).thenReturn(offsetStartMonthRange);

            when(workoutRepository.getMostUsedRoutinesByUserIdAndDateBetween(userId, offsetStartMonthRange.get(0), offsetStartMonthRange.get(1)))
                    .thenReturn(routines);

            List<ResponseRoutineSimpleDto> mostUsedRoutines = workoutStatsService.getMostUsedRoutinesByMonth(10, 3);

            assertEquals(10, mostUsedRoutines.size());
            verify(routineMapper, times(10)).toSimpleDto(any(Routine.class));
        }

    }

    @Nested
    @DisplayName("Get Total Muscle Sets By Week Tests")
    public class GetTotalMuscleSetsByWeekTests {
        List<ZonedDateTime> startAndEndOfTheWeekOffsetRange;
        List<MuscleSetDto> muscleSets;

        @BeforeEach
        void setUp() {
            startAndEndOfTheWeekOffsetRange = List.of(
                    ZonedDateTime.parse("2025-01-01T00:00:00Z"),
                    ZonedDateTime.parse("2025-01-07T23:59:59Z")
            );

            muscleSets = List.of(
                    WorkoutStatsFactory.createMuscleSetDto(List.of("Biceps"), 10L),
                    WorkoutStatsFactory.createMuscleSetDto(List.of("Triceps"), 5L));
        }

        @Test
        void shouldGetTotalMuscleSets() {
            when(offsetDateService.getWeeklyDateRangeOffset(0)).thenReturn(startAndEndOfTheWeekOffsetRange);

            when(workoutRepository.getTotalMuscleSetsByUserIdAndDateBetween(userId,
                    startAndEndOfTheWeekOffsetRange.get(0), startAndEndOfTheWeekOffsetRange.get(1)))
                    .thenReturn(muscleSets);

            List<MuscleSetDto> totalMuscleSets = workoutStatsService.getTotalMuscleSetsByWeek(0);

            assertEquals(2, totalMuscleSets.size());
        }

        @Test
        void shouldGetEmptyTotalMuscleSets() {
            when(offsetDateService.getWeeklyDateRangeOffset(0)).thenReturn(startAndEndOfTheWeekOffsetRange);

            when(workoutRepository.getTotalMuscleSetsByUserIdAndDateBetween(userId,
                    startAndEndOfTheWeekOffsetRange.get(0), startAndEndOfTheWeekOffsetRange.get(1)))
                    .thenReturn(List.of());

            List<MuscleSetDto> totalMuscleSets = workoutStatsService.getTotalMuscleSetsByWeek(0);

            assertEquals(0, totalMuscleSets.size());
        }
    }

    @Nested
    @DisplayName("Check If User Is New Tests")
    public class CheckIfUserIsNewTests {
        List<ResponseWorkoutDto> workouts;
        List<ResponseRoutineDto> routines;

        @BeforeEach
        void setup() {
            workouts = List.of(
                    WorkoutFactory.createResponseWorkoutDto(),
                    WorkoutFactory.createResponseWorkoutDto(),
                    WorkoutFactory.createResponseWorkoutDto()
            );

            routines = List.of(
                    RoutineFactory.createResponseRoutineDto(),
                    RoutineFactory.createResponseRoutineDto(),
                    RoutineFactory.createResponseRoutineDto()
            );
        }

        @Test
        void shouldReturnTrue_WhenWorkoutAndRoutinesListAreEmpty() {
            when(workoutService.getWorkoutsByUserId()).thenReturn(List.of());
            when(routineService.getRoutinesByUserId()).thenReturn(List.of());

            assertTrue(workoutStatsService.checkIfUserIsNew());
        }

        @Test
        void shouldReturnFalse_WhenUserHasWorkoutsAndRoutines() {
            when(workoutService.getWorkoutsByUserId()).thenReturn(workouts);
            when(routineService.getRoutinesByUserId()).thenReturn(routines);

            assertFalse(workoutStatsService.checkIfUserIsNew());
        }


        @Test
        void shouldReturnFalse_WhenUserHasWorkouts() {
            when(workoutService.getWorkoutsByUserId()).thenReturn(workouts);
            when(routineService.getRoutinesByUserId()).thenReturn(List.of());

            assertFalse(workoutStatsService.checkIfUserIsNew());
        }

        @Test
        void shouldReturnFalse_WhenUserHasRoutines() {
            when(workoutService.getWorkoutsByUserId()).thenReturn(List.of());
            when(routineService.getRoutinesByUserId()).thenReturn(routines);

            assertFalse(workoutStatsService.checkIfUserIsNew());
        }
    }
}