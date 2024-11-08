package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout/workout")
public class WorkoutController {
    private final WorkoutService workoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWorkoutDto createWorkout(@Valid @RequestBody CreateWorkoutDto createWorkoutDto) {

        return workoutService.createWorkout(createWorkoutDto);
    }

    @PutMapping("/{id}")
    public ResponseWorkoutDto updateWorkout(
            @Valid @RequestBody CreateWorkoutDto updateWorkoutDto,
            @PathVariable("id") UUID workoutId) {

        return workoutService.updateWorkout(updateWorkoutDto, workoutId);
    }

    @GetMapping("{id}")
    public ResponseWorkoutDto getWorkoutById(@PathVariable("id") UUID workoutId) {

        return workoutService.getWorkoutById(workoutId);
    }

    @GetMapping("/user")
    public List<ResponseWorkoutDto> getWorkoutsByUserId() {

        return workoutService.getWorkoutsByUserId();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkout(@PathVariable("id") UUID workoutId) {

        workoutService.deleteWorkout(workoutId);
    }
}
