package com.sourcery.gymapp.workout.dto;

import java.util.UUID;

public record ResponseRoutineSimpleDto(
        UUID id,
        String name
) {
}
