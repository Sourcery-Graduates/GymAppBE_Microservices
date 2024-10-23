package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.CreateRoutineGridExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineExerciseDto;
import com.sourcery.gymapp.backend.workout.service.RoutineExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workout/routine/exercise")
@RequiredArgsConstructor
public class RoutineExerciseController {
    private final RoutineExerciseService routineExerciseService;

    @PutMapping
    public CreateRoutineGridExerciseDto updateExercisesInRoutine(
            @RequestParam UUID routineId,
            @RequestBody List<CreateRoutineExerciseDto> createRoutineExerciseDto) {

        routineExerciseService.addExerciseToRoutine();
    }


    @GetMapping
    public List<ResponseRoutineExerciseDto> getExercisesFromRoutine() {
        routineExerciseService.getExercisesFromRoutine();
    }

    @DeleteMapping("/{id}")
    public void removeExerciseFromRoutine(@PathVariable UUID id) {
        routineExerciseService.removeExerciseFromRoutine();
    }
}
