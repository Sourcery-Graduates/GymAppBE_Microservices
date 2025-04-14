package com.sourcery.gymapp.workout.dto;

import java.util.HashMap;
import java.util.List;

public record ResponseWorkoutGridGroupedByDate(
        HashMap<String, List<ResponseWorkoutDto>> workouts) {
}
