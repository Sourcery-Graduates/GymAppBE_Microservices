package com.sourcery.gymapp.backend.workout.exception;

public record ErrorResponse(
        String message,
        ErrorCode code
) {
}