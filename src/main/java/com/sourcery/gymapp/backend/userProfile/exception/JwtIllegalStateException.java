package com.sourcery.gymapp.backend.userProfile.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtIllegalStateException extends RuntimeException {
    private final ErrorCode code;
    private final HttpStatus status;

    public JwtIllegalStateException(String message) {
        super(message);
        this.code = ErrorCode.JWT_ILLEGAL_STATE;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
