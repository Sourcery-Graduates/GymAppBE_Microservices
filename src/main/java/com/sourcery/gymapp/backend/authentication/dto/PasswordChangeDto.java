package com.sourcery.gymapp.backend.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeDto(
        @NotBlank(message = "Password1 is mandatory")
        @Size(min = 8, message = "Password1 must be at least 8 characters long")
        String password1,

        @NotBlank(message = "Password2 is mandatory")
        @Size(min = 8, message = "Password2 must be at least 8 characters long")
        String password2,

        @NotBlank(message = "Token is mandatory")
        String token
        )
{ }
