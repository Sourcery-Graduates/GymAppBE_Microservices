package com.sourcery.gymapp.workout.dto;

import java.util.List;

public record MuscleSetDto(
    List<String> primaryMuscles,
    Long numberOfSets
) {
}