package com.sourcery.gymapp.backend.workout.dto;

import java.util.UUID;

public record ExerciseSimpleDto(
        UUID id,
        String name
) {
}
