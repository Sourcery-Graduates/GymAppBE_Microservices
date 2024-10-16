package com.sourcery.gymapp.backend.workout.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ROUTINE_NOT_FOUND("Routine not found"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    USER_NOT_FOUND("User not found");

    private final String code;
}
