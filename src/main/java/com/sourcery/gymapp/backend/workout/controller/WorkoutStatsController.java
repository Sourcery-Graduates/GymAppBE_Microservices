package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.MuscleSetDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineSimpleDto;
import com.sourcery.gymapp.backend.workout.dto.WorkoutStatsDto;
import com.sourcery.gymapp.backend.workout.service.WorkoutStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout/workout-stats")
public class WorkoutStatsController {
    private final WorkoutStatsService workoutStatsService;

    @GetMapping("")
    public List<WorkoutStatsDto> getWorkoutStats() {
        return workoutStatsService.getWorkoutStatsByMonth();
    }

    @GetMapping("/is-user-new")
    public boolean checkIfUserIsNew() {
        return workoutStatsService.checkIfUserIsNew();
    }

    @GetMapping("/most-used")
    public List<ResponseRoutineSimpleDto> getMostUsedRoutines(
            @RequestParam Integer routinesLimit,
            @RequestParam Integer offsetStartMonth) {
        return workoutStatsService.getMostUsedRoutinesByMonth(routinesLimit, offsetStartMonth);
    }

    @GetMapping("/muscle-sets")
    public List<MuscleSetDto> getTotalMuscleSets(
            @RequestParam Integer offsetWeek) {
        return workoutStatsService.getTotalMuscleSetsByWeek(offsetWeek);
    }
}
