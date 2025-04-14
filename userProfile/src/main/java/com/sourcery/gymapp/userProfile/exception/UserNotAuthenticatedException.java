package com.sourcery.gymapp.userProfile.exception;

import org.springframework.http.HttpStatus;

public class UserNotAuthenticatedException extends UserProfileRuntimeException {

    public UserNotAuthenticatedException() {
        super("User has not been authenticated",
                ErrorCode.USER_NOT_AUTHENTICATED,
                HttpStatus.UNAUTHORIZED);
    }
}
