package com.sourcery.gymapp.backend.workout.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class RoutineNotFoundException extends WorkoutRuntimeException {

    public RoutineNotFoundException(UUID id) {
        super("Can't find Routine by ID [%s]".formatted(id),
                ErrorCode.ROUTINE_NOT_FOUND,
                HttpStatus.NOT_FOUND);
    }
}
