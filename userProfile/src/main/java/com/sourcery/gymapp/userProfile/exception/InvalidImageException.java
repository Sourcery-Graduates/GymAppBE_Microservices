package com.sourcery.gymapp.userProfile.exception;

import org.springframework.http.HttpStatus;

public class InvalidImageException extends UserProfileRuntimeException {
    public InvalidImageException(String message, HttpStatus status) {
        super(message, ErrorCode.REQUEST_VALIDATION_ERROR, status);
    }
}
