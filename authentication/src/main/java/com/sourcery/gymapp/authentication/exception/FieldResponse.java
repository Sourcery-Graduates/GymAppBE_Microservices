package com.sourcery.gymapp.authentication.exception;

public record FieldResponse(
        String field,
        String error
) {
}

