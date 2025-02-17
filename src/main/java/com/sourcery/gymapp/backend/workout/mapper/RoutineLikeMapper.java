package com.sourcery.gymapp.backend.workout.mapper;

import com.sourcery.gymapp.backend.events.RoutineLikeEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RoutineLikeMapper {
    public RoutineLikeEvent toAddLikeEvent(UUID userId, UUID routineId) {
        return new RoutineLikeEvent(
                userId,
                routineId,
                true,
                LocalDateTime.now()
        );
    }

    public RoutineLikeEvent toRemoveLikeEvent(UUID userId, UUID routineId) {
        return new RoutineLikeEvent(
                userId,
                routineId,
                false,
                LocalDateTime.now()
        );
    }
}
