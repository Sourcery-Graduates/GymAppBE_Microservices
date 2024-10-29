package com.sourcery.gymapp.backend.workout.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponseRoutineExerciseDto(
        UUID routineExerciseId,
        ExerciseSimpleDto exercise,
        Integer orderNumber,
        Integer defaultSets,
        Integer defaultReps,
        BigDecimal defaultWeight,
        Integer defaultRestTime,
        String notes
) {
}
