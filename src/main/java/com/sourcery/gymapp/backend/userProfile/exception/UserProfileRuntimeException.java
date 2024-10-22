package com.sourcery.gymapp.backend.userProfile.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserProfileRuntimeException extends RuntimeException {
    private final ErrorCode code;
    private final HttpStatus status;

    public UserProfileRuntimeException (String message, ErrorCode code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public UserProfileRuntimeException (String message) {
        super(message);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
