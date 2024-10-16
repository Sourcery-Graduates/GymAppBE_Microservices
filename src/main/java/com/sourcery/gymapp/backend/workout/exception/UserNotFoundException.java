package com.sourcery.gymapp.backend.workout.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends WorkoutRuntimeException {

    public UserNotFoundException(String message) {
        super(message, ErrorCode.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }
}
