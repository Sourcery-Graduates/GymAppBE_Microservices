package com.sourcery.gymapp.workout.controller;
import com.sourcery.gymapp.workout.dto.CreateRoutineExerciseDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineDetailDto;
import com.sourcery.gymapp.workout.service.RoutineExerciseService;
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
    public ResponseRoutineDetailDto updateExercisesInRoutine(
            @RequestParam UUID routineId,
            @Valid @RequestBody List<CreateRoutineExerciseDto> createRoutineExerciseDto) {

        return routineExerciseService.replaceExercisesInRoutine(routineId, createRoutineExerciseDto);
    }


    @GetMapping
    public ResponseRoutineDetailDto getRoutineDetails(
            @RequestParam UUID routineId
    ) {
        return routineExerciseService.getRoutineDetails(routineId);
    }
}
