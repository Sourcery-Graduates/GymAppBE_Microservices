package com.sourcery.gymapp.backend.workout.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateWorkoutExerciseSetDto(

        @Positive
        @NotNull
        Integer setNumber,

        @Positive
        Integer reps,

        @PositiveOrZero
        BigDecimal weight,

        @PositiveOrZero
        Integer restTime,

        @Size(max = 255)
        String comment
) {
}
