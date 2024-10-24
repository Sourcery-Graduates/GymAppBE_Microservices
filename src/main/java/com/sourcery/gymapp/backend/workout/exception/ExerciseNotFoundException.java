package com.sourcery.gymapp.backend.workout.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ExerciseNotFoundException extends WorkoutRuntimeException {
    public ExerciseNotFoundException(UUID id) {
        super("Can't find Exercise by ID [%s]".formatted(id),
                ErrorCode.ROUTINE_NOT_FOUND,
                HttpStatus.NOT_FOUND);
    }
}
