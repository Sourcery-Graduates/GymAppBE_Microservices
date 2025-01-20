package com.sourcery.gymapp.backend.workout.dto;

import java.util.HashMap;
import java.util.List;

public record ResponseWorkoutGridGroupedByDate(
        HashMap<String, List<ResponseWorkoutDto>> workouts) {
}
