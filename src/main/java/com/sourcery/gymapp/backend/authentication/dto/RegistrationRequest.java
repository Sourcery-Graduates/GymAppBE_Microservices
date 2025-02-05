package com.sourcery.gymapp.backend.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "First name is mandatory")
    @Size(max = 64, message = "First name must be at most 64 characters long")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 64, message = "Last name must be at most 64 characters long")
    private String lastName;

    @Size(max = 128, message = "location must be at most 128 characters long")
    private String location;
}
