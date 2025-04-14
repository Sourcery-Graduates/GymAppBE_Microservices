package com.sourcery.gymapp.workout.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResponseRoutineDto(

        UUID id,
        String name,
        String description,
        LocalDateTime createdAt,
        UUID userId,
        long likesCount,
        boolean isLikedByCurrentUser
) {
}
