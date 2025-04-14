package com.sourcery.gymapp.workout.dto;

import java.util.List;

public record ResponseRoutineDetailDto(
        ResponseRoutineDto routine,
        List<ResponseRoutineExerciseDto> exercises
) {
}
