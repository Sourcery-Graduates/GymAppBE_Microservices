package com.sourcery.gymapp.workout.dto;

import com.sourcery.gymapp.workout.model.Routine;

public interface RoutineWithLikeStatusProjection {
    Routine getRoutine();
    boolean isLikedByCurrentUser();
}
