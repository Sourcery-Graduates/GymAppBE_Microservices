package com.sourcery.gymapp.backend.workout.dto;

import java.util.List;
import java.util.UUID;

public record ResponseRoutineExerciseListDto(
        UUID routineId,
        List<ResponseRoutineExerciseDto> exercises
) {
}
