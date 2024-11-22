package com.sourcery.gymapp.backend.workout.factory;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ExerciseSimpleDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutExerciseDto;
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

    public static CreateWorkoutExerciseDto createCreateWorkoutExerciseDto(UUID exerciseId) {
        return new CreateWorkoutExerciseDto(
                UUID.randomUUID(),
                exerciseId,
                1,
                "Test Notes",
                null
        );
    }
}
