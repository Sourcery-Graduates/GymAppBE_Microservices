package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutGridGroupedByDate;
import com.sourcery.gymapp.backend.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
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

    @GetMapping("/date")
    public ResponseWorkoutGridGroupedByDate getWorkoutGridByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate
    ) {
        return workoutService.getWorkoutGridGroupByDate(startDate,endDate);
    }
}
