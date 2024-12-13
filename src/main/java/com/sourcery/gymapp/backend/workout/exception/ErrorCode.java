package com.sourcery.gymapp.backend.workout.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ROUTINE_NOT_FOUND("Routine not found"),
    EXERCISE_NOT_FOUND("Exercise not found"),
    WORKOUT_NOT_FOUND("Workout not found"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    USER_NOT_FOUND("User not found"),
    USER_NOT_AUTHENTICATED("User not authenticated"),
    USER_NOT_AUTHORIZED("User not authorized"),
    VALIDATION_ERROR("Validation error"),
    LIKE_ALREADY_EXISTS("Like already exists"),
    LIKE_NOT_FOUND("Like not found"),
    METHOD_ARGUMENT_TYPE_MISMATCH("Passed argument was incorrect type");

    private final String code;
}
