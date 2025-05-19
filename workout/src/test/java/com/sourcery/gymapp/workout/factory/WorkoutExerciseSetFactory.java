package com.sourcery.gymapp.workout.factory;

import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.dto.ResponseWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.model.WorkoutExercise;
import com.sourcery.gymapp.workout.model.WorkoutExerciseSet;

import java.math.BigDecimal;
import java.util.UUID;

public class WorkoutExerciseSetFactory {

    public static WorkoutExerciseSet createWorkoutExerciseSet() {
        WorkoutExerciseSet set = new WorkoutExerciseSet();
        set.setId(null);
        set.setSetNumber(1);
        set.setReps(5);
        set.setWeight(new BigDecimal(100));
        set.setRestTime(60);
        set.setComment("Test Comment");

        return set;
    }

    public static WorkoutExerciseSet createWorkoutExerciseSet(WorkoutExercise workoutExercise) {
        WorkoutExerciseSet set = new WorkoutExerciseSet();
        set.setWorkoutExercise(workoutExercise);
        set.setId(null);
        set.setSetNumber(1);
        set.setReps(5);
        set.setWeight(new BigDecimal(100));
        set.setRestTime(60);
        set.setComment("Test Comment");

        return set;
    }

    public static ResponseWorkoutExerciseSetDto createResponseWorkoutExerciseSetDto() {
        return new ResponseWorkoutExerciseSetDto(
                null,
                1,
                5,
                new BigDecimal(100),
                60,
                "Test Comment"
        );
    }

    public static CreateWorkoutExerciseSetDto createCreateWorkoutExerciseSetDto() {
        return new CreateWorkoutExerciseSetDto(
                null,
                1,
                5,
                new BigDecimal(100),
                60,
                "Test Comment"
        );
    }

    public static CreateWorkoutExerciseSetDto createCreateWorkoutExerciseSetDto(Integer setNumber) {
        return new CreateWorkoutExerciseSetDto(
                null,
                setNumber,
                5,
                new BigDecimal(100),
                60,
                "Test Comment"
        );
    }
}
