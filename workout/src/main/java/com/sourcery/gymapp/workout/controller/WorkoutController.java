package com.sourcery.gymapp.workout.controller;
import com.sourcery.gymapp.workout.dto.*;
import com.sourcery.gymapp.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.ZonedDateTime;
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

    @Operation(
        summary = "Get workouts grouped by date",
        description = "Retrieves user's workouts within specified date range, grouped by date"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved workouts"),
        @ApiResponse(responseCode = "400", description = "Invalid date format"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication is required")
    })
    @GetMapping("/date")
    public ResponseWorkoutGridGroupedByDate getWorkoutGridByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate
    ) {
        return workoutService.getWorkoutGridGroupByDate(startDate,endDate);
    }
}
