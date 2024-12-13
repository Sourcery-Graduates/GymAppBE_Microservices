package com.sourcery.gymapp.backend.workout.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record CreateWorkoutDto(

        @NotBlank
        @Size(max = 255)
        String name,

        @NotNull
        ZonedDateTime date,

        @Size(max = 255)
        String comment,

        @Valid
        List<CreateWorkoutExerciseDto> exercises,

        UUID routineId,

        UUID basedOnWorkoutId
) {
}
