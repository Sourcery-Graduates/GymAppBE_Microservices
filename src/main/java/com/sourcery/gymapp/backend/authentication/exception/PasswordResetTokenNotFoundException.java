package com.sourcery.gymapp.backend.authentication.exception;

import org.springframework.http.HttpStatus;

public class PasswordResetTokenNotFoundException extends AuthenticationRuntimeException {

    public PasswordResetTokenNotFoundException() {
        super("Password token not found or already used", ErrorCode.PASSWORD_TOKEN_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
