package com.sourcery.gymapp.backend.events;

import java.util.UUID;

public record LikeNotificationEvent(
        UUID ownerId,
        UUID routineId,
        int likesCount
) {
}
