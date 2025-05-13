package com.sourcery.gymapp.workout.integration;

import com.sourcery.gymapp.workout.dto.MuscleSetDto;
import com.sourcery.gymapp.workout.factory.*;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.model.Workout;
import com.sourcery.gymapp.workout.model.WorkoutExercise;
import com.sourcery.gymapp.workout.model.WorkoutExerciseSet;
import com.sourcery.gymapp.workout.repository.ExerciseRepository;
import com.sourcery.gymapp.workout.repository.RoutineRepository;
import com.sourcery.gymapp.workout.repository.WorkoutExerciseRepository;
import com.sourcery.gymapp.workout.repository.WorkoutExerciseSetRepository;
import com.sourcery.gymapp.workout.repository.WorkoutRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkoutRepositoryIntegrationTest extends BaseWorkoutIntegrationJPATest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    RoutineRepository routineRepository;

    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    WorkoutExerciseSetRepository workoutExerciseSetRepository;

    UUID userId;
    List<Routine> routines;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        Exercise exercise1 = ExerciseFactory.createExercise(null, "Chest exercise", List.of("chest"));
        Exercise exercise2 = ExerciseFactory.createExercise(null, "Back exercise", List.of("back"));

        List<Exercise> exercises = exerciseRepository.saveAll(List.of(exercise1, exercise2));

        Routine routine1 = RoutineFactory.createRoutine(
                "routine1", null, userId);

        Routine routine2 = RoutineFactory.createRoutine(
                "routine2", null, userId);

        routines = routineRepository.saveAll(List.of(routine1, routine2));

        Workout workout1 = WorkoutFactory.createWorkout(userId, "workout1", ZonedDateTime.parse("2024-12-01T00:00:00Z"), routines.getFirst());
        Workout workout2 = WorkoutFactory.createWorkout(userId, "workout2", ZonedDateTime.parse("2024-12-02T00:00:00Z"), routines.getFirst());
        Workout workout3 = WorkoutFactory.createWorkout(userId, "workout3", ZonedDateTime.parse("2025-01-01T00:00:00Z"), routines.get(1));
        Workout workout4 = WorkoutFactory.createWorkout(userId, "workout4", ZonedDateTime.parse("2025-01-02T00:00:00Z"), routines.get(1));
        Workout workout5 = WorkoutFactory.createWorkout(userId, "workout5", ZonedDateTime.parse("2025-01-03T00:00:00Z"), routines.get(1));

        List<Workout> workouts = workoutRepository.saveAll(List.of(workout1, workout2, workout3, workout4, workout5));

        WorkoutExercise workoutExercise1 = WorkoutExerciseFactory.createWorkoutExercise(exercises.getFirst(), workouts.getFirst());
        WorkoutExercise workoutExercise2 = WorkoutExerciseFactory.createWorkoutExercise(exercises.get(1), workouts.get(1));

        List<WorkoutExercise> workoutExercises = workoutExerciseRepository.saveAll(List.of(
                workoutExercise1, workoutExercise2
        ));

        WorkoutExerciseSet workoutExerciseSet1 = WorkoutExerciseSetFactory.createWorkoutExerciseSet(workoutExercises.getFirst());
        WorkoutExerciseSet workoutExerciseSet2 = WorkoutExerciseSetFactory.createWorkoutExerciseSet(workoutExercises.getFirst());
        WorkoutExerciseSet workoutExerciseSet3 = WorkoutExerciseSetFactory.createWorkoutExerciseSet(workoutExercises.getFirst());

        WorkoutExerciseSet workoutExerciseSet4 = WorkoutExerciseSetFactory.createWorkoutExerciseSet(workoutExercises.get(1));
        WorkoutExerciseSet workoutExerciseSet5 = WorkoutExerciseSetFactory.createWorkoutExerciseSet(workoutExercises.get(1));
        WorkoutExerciseSet workoutExerciseSet6 = WorkoutExerciseSetFactory.createWorkoutExerciseSet(workoutExercises.get(1));

        workoutExerciseSetRepository.saveAll(List.of(
                workoutExerciseSet1, workoutExerciseSet2, workoutExerciseSet3,
                workoutExerciseSet4, workoutExerciseSet5, workoutExerciseSet6
        ));
    }

    @Test
    void testCountWorkoutsByUserIdAndDateBetween_shouldReturnWorkoutCountFromMonth() {
        int workoutCount = workoutRepository.countWorkoutsByUserIdAndDateBetween(userId,
                ZonedDateTime.parse("2024-12-01T00:00:00Z"),
                ZonedDateTime.parse("2024-12-31T00:00:00Z")
        );

        assertThat(workoutCount).isEqualTo(2);
    }

    @Test
    void testCountWorkoutsByUserIdAndDateBetween_ShouldReturnZero() {
        int workoutCount = workoutRepository.countWorkoutsByUserIdAndDateBetween(userId,
                ZonedDateTime.parse("2025-02-01T00:00:00Z"),
                ZonedDateTime.parse("2025-02-28T00:00:00Z")
        );

        assertThat(workoutCount).isEqualTo(0);
    }


    @Test
    void testGetTotalWeightByUserIdAndDateBetween_shouldReturnTotalWeightFromMonth() {
        Optional<Integer> totalWeight = workoutRepository.getTotalWeightByUserIdAndDateBetween(userId,
                ZonedDateTime.parse("2024-12-01T00:00:00Z"),
                ZonedDateTime.parse("2024-12-31T00:00:00Z")
        );

        assertThat(totalWeight).isPresent();
        assertThat(totalWeight.get()).isEqualTo(3000);
    }

    @Test
    void testGetTotalWeightByUserIdAndDateBetween_shouldReturnEmpty() {
        Optional<Integer> totalWeight = workoutRepository.getTotalWeightByUserIdAndDateBetween(userId,
                ZonedDateTime.parse("2025-01-01T00:00:00Z"),
                ZonedDateTime.parse("2025-01-31T00:00:00Z")
        );

        assertThat(totalWeight).isEmpty();
    }

    @Test
    void testGetTotalMuscleSetsByUserIdAndDateBetween_shouldReturnMuscleSetsFromWeek() {
        List<MuscleSetDto> muscleSets = workoutRepository.getTotalMuscleSetsByUserIdAndDateBetween(userId,
                ZonedDateTime.parse("2024-12-01T00:00:00Z"),
                ZonedDateTime.parse("2024-12-08T00:00:00Z")
        );

        assertThat(muscleSets).hasSize(2);
        assertThat(muscleSets).containsExactlyInAnyOrder(
                new MuscleSetDto(List.of("chest"), 3L),
                new MuscleSetDto(List.of("back"), 3L));
    }

    @Test
    void testGetTotalMuscleSetsByUserIdAndDateBetween_shouldReturnEmpty() {
        List<MuscleSetDto> muscleSets = workoutRepository.getTotalMuscleSetsByUserIdAndDateBetween(userId,
                ZonedDateTime.parse("2025-02-01T00:00:00Z"),
                ZonedDateTime.parse("2025-02-28T00:00:00Z")
        );

        assertThat(muscleSets).isEmpty();
        assertThat(muscleSets).hasSize(0);
    }

    @Test
    void testGetMostUsedRoutinesByUserIdAndDateBetween_shouldReturnMostUsedFromLastThreeMonths() {
        List<Routine> routines = workoutRepository.getMostUsedRoutinesByUserIdAndDateBetween(userId,
                ZonedDateTime.parse("2024-10-05T00:00:00Z"),
                ZonedDateTime.parse("2025-01-05T00:00:00Z"));

        assertThat(routines).hasSize(2);
        assertThat(routines).containsExactly(routines.getFirst(), routines.get(1));
    }

    @Test
    void testGetMostUsedRoutinesByUserIdAndDateBetween_shouldReturnEmpty() {
        List<Routine> routines = workoutRepository.getMostUsedRoutinesByUserIdAndDateBetween(userId,
                ZonedDateTime.parse("2025-01-05T00:00:00Z"),
                ZonedDateTime.parse("2025-04-05T00:00:00Z"));

        assertThat(routines).isEmpty();
        assertThat(routines).hasSize(0);
    }
}
