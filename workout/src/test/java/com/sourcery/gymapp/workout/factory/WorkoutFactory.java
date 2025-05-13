package com.sourcery.gymapp.workout.factory;

import com.sourcery.gymapp.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.model.Workout;
import com.sourcery.gymapp.workout.model.WorkoutExercise;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class WorkoutFactory {

    public static Workout createWorkout(
            UUID userId,
            String name,
            ZonedDateTime date,
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
            ZonedDateTime date,
            Routine routine
    ) {
        Workout workout = new Workout();

        workout.setUserId(userId);
        workout.setName(name);
        workout.setDate(date);
        workout.setComment("Test Comment");
        workout.setRoutine(routine);

        return workout;
    }

    public static Workout createWorkout(
            UUID userId,
            String name,
            ZonedDateTime date
    ) {
        return createWorkout(
                userId,
                name,
                date,
                "comment",
                null,
                null,
                List.of()
        );
    }

    public static Workout createWorkout(
            UUID userId,
            String name,
            ZonedDateTime date,
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
                null
        );
    }

    public static Workout createWorkout(
            String name,
            ZonedDateTime date,
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

    public static Workout createWorkout(
            Workout basedOnWorkout,
            Routine routine,
            List<WorkoutExercise> exercises
    ) {
        return createWorkout(
                UUID.randomUUID(),
                "Test Name",
                LocalDateTime.of(2024, 1, 1, 0, 0)
                        .atZone(ZoneOffset.UTC),
                "Test Comment",
                basedOnWorkout,
                routine,
                exercises
        );
    }

    public static Workout createWorkout() {
        return createWorkout(
                "Test Name",
                LocalDateTime.of(2024, 1, 1, 0, 0)
                        .atZone(ZoneOffset.UTC),
                "Test Comment"
        );
    }

    public static CreateWorkoutDto createCreateWorkoutDto(
            String name,
            ZonedDateTime date,
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
            ZonedDateTime date,
            String comment,
            List<CreateWorkoutExerciseDto> exercises
    ) {
        return new CreateWorkoutDto(
                name,
                date,
                comment,
                exercises,
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static CreateWorkoutDto createCreateWorkoutDto(
            UUID routineId,
            UUID basedOnWorkoutId,
            List<CreateWorkoutExerciseDto> exercises
    ) {
        return new CreateWorkoutDto(
                "Test Name",
                LocalDateTime.of(2024, 1, 1, 0, 0)
                        .atZone(ZoneOffset.UTC),
                "Test Comment",
                exercises,
                routineId,
                basedOnWorkoutId
        );
    }

    public static CreateWorkoutDto createCreateWorkoutDto() {
        return createCreateWorkoutDto(
                "Test Name",
                LocalDateTime.of(2024, 1, 1, 0, 0)
                        .atZone(ZoneOffset.UTC),
                "Test Comment",
                null
        );
    }

    public static ResponseWorkoutDto createResponseWorkoutDto() {
        return new ResponseWorkoutDto(
                null,
                UUID.randomUUID(),
                "Test Name",
                LocalDateTime.of(2024, 1, 1, 0, 0)
                        .atZone(ZoneOffset.UTC),
                "Test Comment",
                null,
                null,
                List.of()
        );
    }
}
