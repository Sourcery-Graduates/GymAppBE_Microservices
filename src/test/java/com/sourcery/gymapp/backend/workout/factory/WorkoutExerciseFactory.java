package com.sourcery.gymapp.backend.workout.factory;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.backend.workout.dto.ExerciseSimpleDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;

import java.util.List;
import java.util.UUID;

public class WorkoutExerciseFactory {

    public static ResponseWorkoutExerciseDto createResponseWorkoutExerciseDto() {
        ExerciseSimpleDto exerciseSimpleDto = new ExerciseSimpleDto(
                UUID.randomUUID(),
                "Test Exercise"
        );

        return new ResponseWorkoutExerciseDto(
                UUID.randomUUID(),
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
                UUID.randomUUID(),
                exerciseId,
                1,
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
        workoutExercise.setId(UUID.randomUUID());
        workoutExercise.setOrderNumber(1);
        workoutExercise.setNotes("Test Notes");
        workoutExercise.setExercise(exercise);

        return workoutExercise;
    }
}
