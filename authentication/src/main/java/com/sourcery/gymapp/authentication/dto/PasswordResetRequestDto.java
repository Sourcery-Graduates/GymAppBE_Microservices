package com.sourcery.gymapp.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequestDto(

        @NotBlank
        @Email(message = "Email should be valid")
        String email
) {}
