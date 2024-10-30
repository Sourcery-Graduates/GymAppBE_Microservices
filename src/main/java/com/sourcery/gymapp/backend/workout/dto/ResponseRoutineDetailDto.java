package com.sourcery.gymapp.backend.workout.dto;

import java.util.List;

public record ResponseRoutineDetailDto(
        ResponseRoutineDto routine,
        List<ResponseRoutineExerciseDto> exercises
) {
}
