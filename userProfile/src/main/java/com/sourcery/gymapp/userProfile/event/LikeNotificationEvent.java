package com.sourcery.gymapp.userProfile.event;

import java.util.UUID;

public record LikeNotificationEvent(
        UUID ownerId,
        UUID routineId,
        String routineName,
        int likesCount
) {
}
