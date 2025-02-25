package com.sourcery.gymapp.backend.authentication.dto;

import com.sourcery.gymapp.backend.authentication.validation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatch(confirmPasswordField = "repeatedPassword")
public record PasswordChangeDto(
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        String repeatedPassword,

        @NotBlank(message = "Token is mandatory")
        String token
        )
{ }
