package com.sourcery.gymapp.backend.workout.dto;

import java.util.List;

public record RoutineGridDto(
        int totalPages,
        List<ResponseRoutineDto> data
) {
}
