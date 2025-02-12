package com.sourcery.gymapp.backend.workout.dto;

import java.util.UUID;

public record ResponseRoutineSimpleDto(
        UUID id,
        String name
) {
}
