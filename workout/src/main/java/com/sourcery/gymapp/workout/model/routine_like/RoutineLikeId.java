package com.sourcery.gymapp.workout.model.routine_like;

import java.io.Serializable;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class RoutineLikeId implements Serializable {

    private UUID routineId;
    private UUID userId;
}
