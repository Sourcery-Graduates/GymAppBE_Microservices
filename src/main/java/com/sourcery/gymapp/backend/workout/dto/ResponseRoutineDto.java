package com.sourcery.gymapp.backend.workout.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResponseRoutineDto(

        UUID id,
        String name,
        String description,
        LocalDateTime localDateTime
) {
}
