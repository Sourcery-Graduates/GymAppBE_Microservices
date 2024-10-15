package com.sourcery.gymapp.backend.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record RoutineDto(

        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 255)
        String description,

        LocalDateTime createdAt
) {
}
