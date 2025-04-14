package com.sourcery.gymapp.userProfile.exception;

import java.util.List;

public record ErrorResponse(
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
