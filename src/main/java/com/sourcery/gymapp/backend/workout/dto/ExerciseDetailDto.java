package com.sourcery.gymapp.backend.workout.dto;

import java.util.List;
import java.util.UUID;

public record ExerciseDetailDto(
        UUID id,
        String name,
        String force,
        String level,
        String mechanic,
        String equipment,
        List<String> primaryMuscles,
        List<String> secondaryMuscles,
        List<String> description,
        String category,
        List<String> images
) {
}
