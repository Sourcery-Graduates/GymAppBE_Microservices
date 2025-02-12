package com.sourcery.gymapp.backend.workout.dto;

import java.util.List;

public record MuscleSetDto(
    List<String> primaryMuscles,
    Long numberOfSets
) {
}