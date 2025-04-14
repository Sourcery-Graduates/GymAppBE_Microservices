package com.sourcery.gymapp.authentication.exception;

import org.springframework.http.HttpStatus;

public class TokenAlreadyInUsageException extends AuthenticationRuntimeException {

    public TokenAlreadyInUsageException() {
        super("Token is already processed by other request", ErrorCode.TOKEN_ALREADY_IN_USAGE_EXCEPTION, HttpStatus.BAD_REQUEST);
    }

    public TokenAlreadyInUsageException(String message) {
        super(message, ErrorCode.TOKEN_ALREADY_IN_USAGE_EXCEPTION, HttpStatus.BAD_REQUEST);
    }

}