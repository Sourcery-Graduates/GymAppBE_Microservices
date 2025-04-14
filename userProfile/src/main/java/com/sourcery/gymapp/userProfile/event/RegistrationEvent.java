package com.sourcery.gymapp.userProfile.event;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistrationEvent(
        @NotNull
        UUID userId,
        @NotNull
        String username,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        String location,
        String bio
) {
}
