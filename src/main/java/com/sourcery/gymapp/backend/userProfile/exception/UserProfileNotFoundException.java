package com.sourcery.gymapp.backend.userProfile.exception;

import org.springframework.http.HttpStatus;

public class UserProfileNotFoundException extends UserProfileRuntimeException {

    public UserProfileNotFoundException() {
        super("User has not been found",
                ErrorCode.USER_POFILE_NOT_FOUND,
                HttpStatus.NOT_FOUND);
    }
}