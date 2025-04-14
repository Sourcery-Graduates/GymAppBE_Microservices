package com.sourcery.gymapp.workout.event;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoutineLikeEvent(
        @NotNull
        UUID userId,
        @NotNull
        UUID routineId,
        @NotNull
        String routineName,
        @NotNull
        UUID ownerId,
        @NotNull
        boolean isLiked,
        @NotNull
        LocalDateTime createdAt
) {
}
