package com.sourcery.gymapp.backend.userProfile.exception;

import org.springframework.http.HttpStatus;

public class UserNotAuthenticatedException extends UserProfileRuntimeException {

    public UserNotAuthenticatedException() {
        super("User has not been authenticated",
                ErrorCode.USER_NOT_AUTHENTICATED,
                HttpStatus.UNAUTHORIZED);
    }
}
