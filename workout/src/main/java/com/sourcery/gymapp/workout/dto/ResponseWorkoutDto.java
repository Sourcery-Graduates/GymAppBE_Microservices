package com.sourcery.gymapp.workout.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record ResponseWorkoutDto(
        UUID id,
        UUID userId,
        String name,
        ZonedDateTime date,
        String comment,
        UUID basedOnWorkoutId,
        UUID routineId,
        List<ResponseWorkoutExerciseDto> exercises
) {
}
