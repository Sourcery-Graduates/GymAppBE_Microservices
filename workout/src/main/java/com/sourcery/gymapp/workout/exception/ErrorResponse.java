package com.sourcery.gymapp.workout.exception;

public record ErrorResponse(
        String message,
        ErrorCode code
) {
}