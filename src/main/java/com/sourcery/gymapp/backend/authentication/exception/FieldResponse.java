package com.sourcery.gymapp.backend.authentication.exception;

public record FieldResponse(
        String field,
        String error
) {
}

