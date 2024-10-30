package com.sourcery.gymapp.backend.workout.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotAuthenticatedException extends WorkoutRuntimeException {

    public UserNotAuthenticatedException() {
        super("User is not authenticated",
                ErrorCode.USER_NOT_AUTHENTICATED,
                HttpStatus.UNAUTHORIZED
        );
    }
}
