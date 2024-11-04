package com.sourcery.gymapp.backend.workout.dto;

import java.util.List;

public record ExercisePageDto(
        int totalPages,
        long totalElements,
        List<ExerciseDetailDto> data
) {
}
