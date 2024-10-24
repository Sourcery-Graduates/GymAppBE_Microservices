package com.sourcery.gymapp.backend.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateRoutineExerciseDto(

        @NotBlank
        UUID exerciseId,

        @NotBlank
        Integer orderNumber,

        @NotBlank
        Integer defaultSets,

        @NotBlank
        Integer defaultReps,

        @NotBlank
        BigDecimal defaultWeight,

        @NotBlank
        Integer defaultRestTime,

        @Size(max = 1000)
        String notes
) {
}