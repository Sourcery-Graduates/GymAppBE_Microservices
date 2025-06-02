package com.sourcery.gymapp.workout.exception;

import org.springframework.http.HttpStatus;

public class UserIdHeaderNotFoundException extends WorkoutRuntimeException {

    public UserIdHeaderNotFoundException() {
        super("Missing X-User-Id Header", ErrorCode.USER_ID_HEADER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
