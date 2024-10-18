package com.sourcery.gymapp.backend.userProfile.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserProfileRuntimeException {

    public UserNotFoundException() {
        super("User has not been found",
                ErrorCode.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND);
    }
}
