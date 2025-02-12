package com.sourcery.gymapp.backend.workout.dto;

import java.util.UUID;

public record WorkoutStatsDto(
        UUID id,
        String type,
        String content
) {
}
