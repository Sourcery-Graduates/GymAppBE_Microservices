package com.sourcery.gymapp.workout.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateRoutineExerciseListDto(

        @NotNull
        UUID routineId,
        List<CreateRoutineExerciseDto> exercises
) {
}
