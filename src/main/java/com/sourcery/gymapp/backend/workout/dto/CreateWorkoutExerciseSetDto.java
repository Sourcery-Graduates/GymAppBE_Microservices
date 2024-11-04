package com.sourcery.gymapp.backend.workout.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateWorkoutExerciseSetDto(

        @NotNull
        Integer setNumber,

        Integer reps,

        BigDecimal weight,

        Integer restTime,

        @Size(max = 255)
        String comment
) {
}
