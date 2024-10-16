package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.service.RoutineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout/routine")
public class RoutineController {
    private final RoutineService routineService;

    @PostMapping
    public ResponseRoutineDto createRoutine(
        @Valid @RequestBody CreateRoutineDto routineDto) {

        return routineService.createRoutine(routineDto);
    }

    @GetMapping("/{id}")
    public ResponseRoutineDto getRoutineById(
        @PathVariable("id") UUID routineId) {

        return routineService.getRoutineById(routineId);
    }

    @GetMapping("/user/{userId}")
    public List<ResponseRoutineDto> getRoutinesByUserId(
        @PathVariable("userId") UUID userId) {

        return routineService.getRoutinesByUserId(userId);
    }

    @PutMapping("/{id}")
    public ResponseRoutineDto updateRoutine(
        @PathVariable("id") UUID routineId,
        @Valid @RequestBody CreateRoutineDto routineDto) {

        return routineService.updateRoutine(routineId, routineDto);
    }

    @DeleteMapping("/{id}")
    public void deleteRoutine(
        @PathVariable("id") UUID id) {

        routineService.deleteRoutine(id);
    }
}
