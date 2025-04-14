package com.sourcery.gymapp.userProfile.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserProfileNotFoundException extends UserProfileRuntimeException {

    public UserProfileNotFoundException(UUID id) {
        super("User Profile of userId: %s has not been found".formatted(id.toString()),
                ErrorCode.USER_PROFILE_NOT_FOUND,
                HttpStatus.NOT_FOUND);
    }
}