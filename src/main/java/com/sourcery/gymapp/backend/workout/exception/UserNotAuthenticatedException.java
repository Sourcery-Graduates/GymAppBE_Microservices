package com.sourcery.gymapp.backend.workout.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotAuthenticatedException extends RuntimeException {
    private final ErrorCode code;
    private final HttpStatus status;

    public UserNotAuthenticatedException() {
        super("User is not authenticated");
        this.code = ErrorCode.USER_NOT_AUTHENTICATED;
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
