package com.sourcery.gymapp.backend.common.domain;

import com.sourcery.gymapp.backend.authentication.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistrationEvent(
        @NotNull
        UUID eventID,
        @Valid
        User user
) {
}
