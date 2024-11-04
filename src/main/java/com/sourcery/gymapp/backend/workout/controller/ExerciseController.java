package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.ExercisePageDto;
import com.sourcery.gymapp.backend.workout.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public ExercisePageDto getPagedExercises(
            @ParameterObject @PageableDefault(size = 10, sort = "name") Pageable pageable,
            @RequestParam(required = false) String prefix) {

        return exerciseService.getExercisesByPrefix(prefix, pageable);
    }

}
