package com.sourcery.gymapp.backend.workout.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;

public class LikeNotFoundException extends WorkoutRuntimeException {
    public LikeNotFoundException(UUID routineId, UUID userId) {
        super(
                "The like for routine with ID [%s] not found by user with ID [%s]"
                        .formatted(routineId, userId),
                ErrorCode.LIKE_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }
}
