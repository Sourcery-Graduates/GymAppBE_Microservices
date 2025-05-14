package com.sourcery.gymapp.workout.factory;

import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.dto.ExerciseSimpleDto;
import com.sourcery.gymapp.workout.dto.ResponseWorkoutExerciseDto;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.Workout;
import com.sourcery.gymapp.workout.model.WorkoutExercise;

import java.util.List;
import java.util.UUID;

public class WorkoutExerciseFactory {

    public static ResponseWorkoutExerciseDto createResponseWorkoutExerciseDto() {
        ExerciseSimpleDto exerciseSimpleDto = new ExerciseSimpleDto(
                null,
                "Test Exercise"
        );

        return new ResponseWorkoutExerciseDto(
                null,
                exerciseSimpleDto,
                1,
                "Test Notes",
                List.of()
        );
    }

    public static CreateWorkoutExerciseDto createCreateWorkoutExerciseDto(
            UUID exerciseId,
            List<CreateWorkoutExerciseSetDto> sets
    ) {
        return new CreateWorkoutExerciseDto(
                null,
                exerciseId,
                1,
                "Test Notes",
                sets
        );
    }

    public static CreateWorkoutExerciseDto createCreateWorkoutExerciseDto(
            Integer orderNumber,
            List<CreateWorkoutExerciseSetDto> sets
    ) {
        return new CreateWorkoutExerciseDto(
                null,
                UUID.randomUUID(),
                orderNumber,
                "Test Notes",
                sets
        );
    }

    public static CreateWorkoutExerciseDto createCreateWorkoutExerciseDto(UUID exerciseId) {
        return createCreateWorkoutExerciseDto(
                exerciseId,
                List.of()
        );
    }

    public static WorkoutExercise createWorkoutExercise(Exercise exercise) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setId(null);
        workoutExercise.setOrderNumber(1);
        workoutExercise.setNotes("Test Notes");
        workoutExercise.setExercise(exercise);

        return workoutExercise;
    }

    public static WorkoutExercise createWorkoutExercise(Exercise exercise, Workout workout) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setWorkout(workout);
        workoutExercise.setId(null);
        workoutExercise.setOrderNumber(1);
        workoutExercise.setNotes("Test Notes");
        workoutExercise.setExercise(exercise);

        return workoutExercise;
    }

    public static WorkoutExercise createWorkoutExerciseWithRandomId(Exercise exercise) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setId(UUID.randomUUID());
        workoutExercise.setOrderNumber(1);
        workoutExercise.setNotes("Test Notes");
        workoutExercise.setExercise(exercise);
        return workoutExercise;
    }

    public static CreateWorkoutExerciseDto createCreateWorkoutExerciseDtoWithRandomId(UUID exerciseId) {
        return new CreateWorkoutExerciseDto(UUID.randomUUID(), exerciseId, 1, "Test Notes", List.of());
    }
}
