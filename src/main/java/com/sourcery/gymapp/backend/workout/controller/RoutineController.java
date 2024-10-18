package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.RoutineGridDto;
import com.sourcery.gymapp.backend.workout.service.RoutineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user")
    public List<ResponseRoutineDto> getRoutinesByUserId() {

        return routineService.getRoutinesByUserId();
    }

    @GetMapping
    public RoutineGridDto getPagedRoutines(
            @PageableDefault(size = 20, sort = "name") Pageable pageable,
            @RequestParam(required = false) String name) {

        return routineService.searchRoutines(name, pageable);
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
