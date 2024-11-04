package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.ExerciseDetailDto;
import com.sourcery.gymapp.backend.workout.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public List<ExerciseDetailDto> getExercisesByPrefix(
            @RequestParam String prefix,
            @RequestParam(required = false) Integer limit) {
        return exerciseService.getExercisesByPrefix(prefix, limit);
    }
}
