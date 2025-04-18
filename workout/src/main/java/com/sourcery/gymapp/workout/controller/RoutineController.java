package com.sourcery.gymapp.workout.controller;
import com.sourcery.gymapp.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.workout.dto.RoutinePageDto;
import com.sourcery.gymapp.workout.service.RoutineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public RoutinePageDto getPagedRoutines(
            @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable,
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
