package com.sourcery.gymapp.workout.dto;

import java.util.UUID;

public record WorkoutStatsDto(
        UUID id,
        String type,
        String content
) {
}
