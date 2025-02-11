package com.sourcery.gymapp.backend.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeDto(
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "Repeated password is mandatory")
        @Size(min = 8, message = "RepeatedPassword must be at least 8 characters long")
        String repeatedPassword,

        @NotBlank(message = "Token is mandatory")
        String token
        )
{ }
