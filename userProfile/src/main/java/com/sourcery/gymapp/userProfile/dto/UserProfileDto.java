package com.sourcery.gymapp.userProfile.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record UserProfileDto(
    @NotBlank
    String username,

    @NotBlank
    String firstName,

    @NotBlank
    String lastName,

    String bio,

    String avatarUrl,

    String location,

    Map<String, Object> settings
) {
}
