package com.sourcery.gymapp.backend.workout.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateRoutineExerciseDto(

        @NotNull
        UUID exerciseId,

        @NotNull
        Integer orderNumber,

        @NotNull
        Integer defaultSets,

        @NotNull
        Integer defaultReps,

        @NotNull
        BigDecimal defaultWeight,

        @NotNull
        Integer defaultRestTime,

        @Size(max = 1000)
        String notes
) {
}