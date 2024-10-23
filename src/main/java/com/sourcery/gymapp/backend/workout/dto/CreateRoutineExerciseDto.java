package com.sourcery.gymapp.backend.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateRoutineExerciseDto(

        @NotBlank
        UUID exerciseId,

        @NotBlank
        int orderNumber,

        @NotBlank
        int defaultSets,

        @NotBlank
        int defaultReps,

        @NotBlank
        int defaultWeight,

        @NotBlank
        int defaultRestTime,

        @Size(max = 1000)
        String notes
) {
}