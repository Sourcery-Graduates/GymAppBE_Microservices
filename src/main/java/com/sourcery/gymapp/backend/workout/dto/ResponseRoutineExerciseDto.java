package com.sourcery.gymapp.backend.workout.dto;

import java.util.UUID;

public record ResponseRoutineExerciseDto(
        UUID exerciseId,
        ExerciseSimpleDto exercise,
        int orderNumber,
        int defaultSets,
        int defaultReps,
        int defaultWeight,
        int defaultRestTime,
        String notes
) {
}
