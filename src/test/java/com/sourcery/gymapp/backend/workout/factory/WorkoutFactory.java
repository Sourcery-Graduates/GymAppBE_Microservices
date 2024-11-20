package com.sourcery.gymapp.backend.workout.factory;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.Workout;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WorkoutFactory {

    public static Workout createWorkout(
            UUID userId,
            String name,
            Date date,
            String comment,
            Workout basedOnWorkout,
            Routine routine,
            List<WorkoutExercise> exercises,
            UUID id
    ) {
        Workout workout = new Workout();

        workout.setUserId(userId);
        workout.setName(name);
        workout.setDate(date);
        workout.setComment(comment);
        workout.setBasedOnWorkout(basedOnWorkout);
        workout.setRoutine(routine);
        workout.setExercises(exercises);
        workout.setId(id);

        return workout;
    }

    public static Workout createWorkout(
            UUID userId,
            String name,
            Date date,
            String comment,
            Workout basedOnWorkout,
            Routine routine,
            List<WorkoutExercise> exercises
    ) {
        return createWorkout(
                userId,
                name,
                date,
                comment,
                basedOnWorkout,
                routine,
                exercises,
                UUID.randomUUID()
        );
    }

    public static Workout createWorkout(
            String name,
            Date date,
            String comment
    ) {
        return createWorkout(
                UUID.randomUUID(),
                name,
                date,
                comment,
                null,
                null,
                List.of()
        );
    }

    public static Workout createWorkout() {
        return createWorkout(
                "Test Name",
                new Date(124, Calendar.JANUARY, 1),
                "Test Comment"
        );
    }

    public static CreateWorkoutDto createCreateWorkoutDto(
            String name,
            Date date,
            String comment,
            UUID routineId,
            UUID basedOnWorkoutId
    ) {
        return new CreateWorkoutDto(
                name,
                date,
                comment,
                null,
                routineId,
                basedOnWorkoutId
        );
    }

    public static CreateWorkoutDto createCreateWorkoutDto(
            String name,
            Date date,
            String comment,
            List<CreateWorkoutExerciseDto> exercises
    ) {
        return new CreateWorkoutDto(
                name,
                date,
                comment,
                exercises,
                null,
                null
        );
    }

    public static CreateWorkoutDto createCreateWorkoutDto() {
        return createCreateWorkoutDto(
                "Test Name",
                new Date(124, Calendar.JANUARY, 1),
                "Test Comment",
                null
        );
    }

    public static ResponseWorkoutDto createResponseWorkoutDto() {
        return new ResponseWorkoutDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test Name",
                new Date(124, Calendar.JANUARY, 1),
                "Test Comment",
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of()
        );
    }
}
