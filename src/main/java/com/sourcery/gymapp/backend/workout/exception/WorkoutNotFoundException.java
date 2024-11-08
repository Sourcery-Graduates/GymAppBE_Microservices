package com.sourcery.gymapp.backend.workout.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class WorkoutNotFoundException extends WorkoutRuntimeException {

    public WorkoutNotFoundException(UUID id) {
        super("Can't find Workout by ID [%s]".formatted(id),
                ErrorCode.WORKOUT_NOT_FOUND,
                HttpStatus.NOT_FOUND);
    }
}
