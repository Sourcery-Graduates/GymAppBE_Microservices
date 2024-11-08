package com.sourcery.gymapp.backend.workout.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateWorkoutExerciseDto(

        UUID id,

        @NotNull
        UUID exerciseId,

        @Positive
        @NotNull
        Integer orderNumber,

        @Size(max = 255)
        String notes,

        List<CreateWorkoutExerciseSetDto> sets
) {
}
