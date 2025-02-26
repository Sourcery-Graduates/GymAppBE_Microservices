package com.sourcery.gymapp.backend.userProfile.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LikeNotificationDto (
        UUID ownerId,
        UUID routineId,
        int likesCount,
        String routineTitle,
        LocalDateTime createdAt
) {
}
