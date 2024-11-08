package com.sourcery.gymapp.backend.workout.dto;

import java.util.List;
import java.util.UUID;

public record ResponseWorkoutExerciseDto(
        UUID id,
        ExerciseSimpleDto exercise,
        Integer orderNumber,
        String notes,
        List<ResponseWorkoutExerciseSetDto> sets
) {
}
