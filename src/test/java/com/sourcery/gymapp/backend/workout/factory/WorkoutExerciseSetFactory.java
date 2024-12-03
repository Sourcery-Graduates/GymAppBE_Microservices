package com.sourcery.gymapp.backend.workout.factory;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutExerciseSetDto;
import com.sourcery.gymapp.backend.workout.model.WorkoutExerciseSet;

import java.math.BigDecimal;
import java.util.UUID;

public class WorkoutExerciseSetFactory {

    public static WorkoutExerciseSet createWorkoutExerciseSet() {
        WorkoutExerciseSet set = new WorkoutExerciseSet();
        set.setId(UUID.randomUUID());
        set.setSetNumber(1);
        set.setReps(5);
        set.setWeight(new BigDecimal(100));
        set.setRestTime(60);
        set.setComment("Test Comment");

        return set;
    }

    public static ResponseWorkoutExerciseSetDto createResponseWorkoutExerciseSetDto() {
        return new ResponseWorkoutExerciseSetDto(
                UUID.randomUUID(),
                1,
                5,
                new BigDecimal(100),
                60,
                "Test Comment"
        );
    }

    public static CreateWorkoutExerciseSetDto createCreateWorkoutExerciseSetDto() {
        return new CreateWorkoutExerciseSetDto(
                UUID.randomUUID(),
                1,
                5,
                new BigDecimal(100),
                60,
                "Test Comment"
        );
    }

    public static CreateWorkoutExerciseSetDto createCreateWorkoutExerciseSetDto(Integer setNumber) {
        return new CreateWorkoutExerciseSetDto(
                UUID.randomUUID(),
                setNumber,
                5,
                new BigDecimal(100),
                60,
                "Test Comment"
        );
    }
}
