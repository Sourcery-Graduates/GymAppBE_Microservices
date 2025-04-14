package com.sourcery.gymapp.authentication.exception;

import org.springframework.http.HttpStatus;

public class RegistrationTokenNotFoundException extends AuthenticationRuntimeException {

    public RegistrationTokenNotFoundException() {
        super("Registration token not found or already used", ErrorCode.REGISTRATION_TOKEN_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
