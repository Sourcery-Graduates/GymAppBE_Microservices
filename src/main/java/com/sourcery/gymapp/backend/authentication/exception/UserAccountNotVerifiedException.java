package com.sourcery.gymapp.backend.authentication.exception;

import org.springframework.http.HttpStatus;

public class UserAccountNotVerifiedException  extends AuthenticationRuntimeException {

    public UserAccountNotVerifiedException() {
        super("Verify your account via email link before logging in", ErrorCode.USER_ACCOUNT_NOT_VERIFIED, HttpStatus.UNAUTHORIZED);
    }

    public UserAccountNotVerifiedException(String message) {
        super(message, ErrorCode.USER_ACCOUNT_NOT_VERIFIED, HttpStatus.UNAUTHORIZED);
    }

}
