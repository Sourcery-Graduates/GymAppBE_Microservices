package com.sourcery.gymapp.backend.workout.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateRoutineGridExerciseDto(

        @NotNull
        UUID routineId,
        List<CreateRoutineExerciseDto> exercises
) {
}
