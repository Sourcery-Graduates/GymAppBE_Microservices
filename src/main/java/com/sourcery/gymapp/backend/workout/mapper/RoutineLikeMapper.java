package com.sourcery.gymapp.backend.workout.mapper;

import com.sourcery.gymapp.backend.events.RoutineLikeEvent;
import com.sourcery.gymapp.backend.workout.model.Routine;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RoutineLikeMapper {
    public RoutineLikeEvent toAddLikeEvent(UUID userId, Routine routine) {
        return new RoutineLikeEvent(
                userId,
                routine.getId(),
                routine.getName(),
                routine.getUserId(),
                true,
                LocalDateTime.now()
        );
    }

    public RoutineLikeEvent toRemoveLikeEvent(UUID userId, Routine routine) {
        return new RoutineLikeEvent(
                userId,
                routine.getId(),
                routine.getName(),
                routine.getUserId(),
                false,
                LocalDateTime.now()
        );
    }
}
