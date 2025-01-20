package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.exception.UserNotAuthorizedException;
import com.sourcery.gymapp.backend.workout.exception.WorkoutNotFoundException;
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

/**
 * REST controller for managing workout operations.
 * Provides endpoints for CRUD operations on workouts.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout/workout")
public class WorkoutController {
    private final WorkoutService workoutService;

    /**
     * Creates a new workout.
     *
     * @param createWorkoutDto DTO containing workout details
     * @return created workout details
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWorkoutDto createWorkout(@Valid @RequestBody CreateWorkoutDto createWorkoutDto) {

        return workoutService.createWorkout(createWorkoutDto);
    }

    /**
     * Updates an existing workout.
     *
     * @param updateWorkoutDto DTO containing updated workout details
     * @param workoutId ID of the workout to update
     * @return updated workout details
     * @throws WorkoutNotFoundException if workout not found
     * @throws UserNotAuthorizedException if user not authorized
     */
    @PutMapping("/{id}")
    public ResponseWorkoutDto updateWorkout(
            @Valid @RequestBody CreateWorkoutDto updateWorkoutDto,
            @PathVariable("id") UUID workoutId) {

        return workoutService.updateWorkout(updateWorkoutDto, workoutId);
    }

    @GetMapping("/{id}")
    public ResponseWorkoutDto getWorkoutById(@PathVariable("id") UUID workoutId) {

        return workoutService.getWorkoutById(workoutId);
    }

    @GetMapping("/user")
    public List<ResponseWorkoutDto> getWorkoutsByUserId() {

        return workoutService.getWorkoutsByUserId();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkout(@PathVariable("id") UUID workoutId) {

        workoutService.deleteWorkout(workoutId);
    }
}
