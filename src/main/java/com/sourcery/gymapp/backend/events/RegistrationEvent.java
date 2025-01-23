package com.sourcery.gymapp.backend.events;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistrationEvent(
        @NotNull
        UUID userId,
        @NotNull
        String username
) {
}
