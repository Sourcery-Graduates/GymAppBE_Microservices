package com.sourcery.gymapp.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRoutineDto(

        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 3000)
        String description
) {
}
