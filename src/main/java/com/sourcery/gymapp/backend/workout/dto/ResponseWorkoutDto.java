package com.sourcery.gymapp.backend.workout.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ResponseWorkoutDto(
        UUID id,
        String name,
        Date date,
        String comment,
        List<ResponseWorkoutExerciseDto> exercises
) {
}
