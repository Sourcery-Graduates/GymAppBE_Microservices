package com.sourcery.gymapp.workout.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;

public class LikeAlreadyExistsException extends WorkoutRuntimeException {
    public LikeAlreadyExistsException(UUID routineId, UUID userId) {
        super(
                "The like for routine with ID [%s] already exists by user with ID [%s]"
                        .formatted(routineId, userId),
                ErrorCode.LIKE_ALREADY_EXISTS,
                HttpStatus.CONFLICT
        );
    }
}
