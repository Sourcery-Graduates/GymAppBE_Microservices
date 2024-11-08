package com.sourcery.gymapp.backend.workout.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponseWorkoutExerciseSetDto(
        UUID id,
        Integer setNumber,
        Integer reps,
        BigDecimal weight,
        Integer restTime,
        String comment
) {
}
