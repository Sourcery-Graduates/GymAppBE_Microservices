package com.sourcery.gymapp.workout.dto;

import java.util.UUID;

public record ExerciseSimpleDto(
        UUID id,
        String name
) {
}
