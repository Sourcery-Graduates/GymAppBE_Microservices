package com.sourcery.gymapp.workout.exception;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ExerciseNotFoundException extends WorkoutRuntimeException {
    public ExerciseNotFoundException(UUID id) {
        super("Can't find Exercise by ID [%s]".formatted(id),
                ErrorCode.EXERCISE_NOT_FOUND,
                HttpStatus.NOT_FOUND);
    }

    public ExerciseNotFoundException(Set<UUID> ids) {
        super("Can't find Exercises by IDs [%s]".formatted(String.join(", ",
                ids.stream().map(UUID::toString).toList())),
                ErrorCode.EXERCISE_NOT_FOUND,
                HttpStatus.NOT_FOUND);
    }
}
