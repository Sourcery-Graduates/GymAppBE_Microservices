package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout/workout")
public class WorkoutController {
    private final WorkoutService workoutService;

    @PostMapping
    public ResponseWorkoutDto createWorkout(@Valid @RequestBody CreateWorkoutDto createWorkoutDto) {

        return workoutService.createWorkout(createWorkoutDto);
    }
}
