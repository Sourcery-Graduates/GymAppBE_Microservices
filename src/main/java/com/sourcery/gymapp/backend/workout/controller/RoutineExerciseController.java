package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.CreateRoutineExerciseListDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineListExerciseDto;
import com.sourcery.gymapp.backend.workout.service.RoutineExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workout/routine/exercise")
@RequiredArgsConstructor
public class RoutineExerciseController {
    private final RoutineExerciseService routineExerciseService;

    @PutMapping
    public ResponseRoutineListExerciseDto updateExercisesInRoutine(
            @RequestParam UUID routineId,
            @Valid @RequestBody List<CreateRoutineExerciseDto> createRoutineExerciseDto) {

        return routineExerciseService.replaceExercisesInRoutine(routineId, createRoutineExerciseDto);
    }


    @GetMapping
    public ResponseRoutineListExerciseDto getExercisesFromRoutine(
            @RequestParam UUID routineId
    ) {
        return routineExerciseService.getExercisesFromRoutine(routineId);
    }
}
