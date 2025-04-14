package com.sourcery.gymapp.workout.exception;

import org.springframework.http.HttpStatus;

public class UserNotAuthorizedException extends WorkoutRuntimeException {
    public UserNotAuthorizedException() {
        super("User is not authorized",
                ErrorCode.USER_NOT_AUTHORIZED,
                HttpStatus.FORBIDDEN
        );
    }
}
