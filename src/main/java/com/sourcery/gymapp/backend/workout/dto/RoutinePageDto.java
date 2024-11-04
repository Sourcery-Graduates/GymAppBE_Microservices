package com.sourcery.gymapp.backend.workout.dto;

import java.util.List;

public record RoutinePageDto(
        int totalPages,
        long totalElements,
        List<ResponseRoutineDto> data
) {
}
