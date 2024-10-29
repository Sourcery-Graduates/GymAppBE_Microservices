package com.sourcery.gymapp.backend.workout.exception;

import java.util.List;

public record FieldErrorResponse(
        String message,
        ErrorCode code,
        List<FieldResponse> fields
) {
}

record FieldResponse(
        String field,
        String error
) {
}
