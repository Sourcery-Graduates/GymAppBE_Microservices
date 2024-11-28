package com.sourcery.gymapp.backend.workout.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record ResponseWorkoutDto(
        UUID id,
        String name,
        ZonedDateTime date,
        String comment,
        List<ResponseWorkoutExerciseDto> exercises
) {
}
