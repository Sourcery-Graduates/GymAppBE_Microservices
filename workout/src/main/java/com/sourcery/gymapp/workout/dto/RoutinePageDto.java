package com.sourcery.gymapp.workout.dto;

import java.util.List;

public record RoutinePageDto(
        int totalPages,
        long totalElements,
        List<ResponseRoutineDto> data
) {
}
