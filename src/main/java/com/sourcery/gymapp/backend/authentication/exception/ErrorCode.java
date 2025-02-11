package com.sourcery.gymapp.backend.authentication.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS"),
    USER_NOT_AUTHENTICATED("USER_NOT_AUTHENTICATED"),
    REQUEST_VALIDATION_ERROR("REQUEST_VALIDATION_ERROR"),
    OAUTH2_ERROR("OAUTH2_ERROR");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
