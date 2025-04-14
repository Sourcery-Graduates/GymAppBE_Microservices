package com.sourcery.gymapp.workout.dto;

import java.util.List;

public record ExercisePageDto(
        int totalPages,
        long totalElements,
        List<ExerciseDetailDto> data
) {
}
