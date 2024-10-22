package com.sourcery.gymapp.backend.userProfile.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    USER_NOT_AUTHENTICATED("USER_NOT_AUTHENTICATED"),
    REQUEST_VALIDATION_ERROR("REQUEST_VALIDATION_ERROR"),
    USER_PROFILE_NOT_FOUND("USER_PROFILE_NOT_FOUND"),
    JWT_ILLEGAL_STATE("JWT_ILLEGAL_STATE");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
