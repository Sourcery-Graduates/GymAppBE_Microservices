package com.sourcery.gymapp.backend.workout.dto;

import com.sourcery.gymapp.backend.workout.model.Routine;

public interface RoutineWithLikeStatusProjection {
    Routine getRoutine();
    boolean isLikedByCurrentUser();
}
