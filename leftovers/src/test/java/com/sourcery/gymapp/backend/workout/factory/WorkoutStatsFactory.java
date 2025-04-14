package com.sourcery.gymapp.workout.factory;

import com.sourcery.gymapp.backend.workout.dto.MuscleSetDto;

import java.util.List;

public class WorkoutStatsFactory {

    public static MuscleSetDto createMuscleSetDto(
            List<String> primaryMuscles,
            Long numberOfSets
    ) {
        return new MuscleSetDto(primaryMuscles, numberOfSets);
    }
}
