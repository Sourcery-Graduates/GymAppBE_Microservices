package com.sourcery.gymapp.backend.workout.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ResponseWorkoutDto(
        UUID id,
        UUID userId,
        String name,
        Date date,
        String comment,
        UUID basedOnWorkoutId,
        UUID routineId,
        List<ResponseWorkoutExerciseDto> exercises
) {
}
