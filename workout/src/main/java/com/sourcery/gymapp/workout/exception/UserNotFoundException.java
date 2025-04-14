package com.sourcery.gymapp.workout.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends WorkoutRuntimeException {

    public UserNotFoundException(String message) {
        super(message, ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException() {
        super("Can't find User", ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
