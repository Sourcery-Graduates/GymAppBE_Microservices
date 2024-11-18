package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.service.RoutineLikeService;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout/routine/like")
public class RoutineLikeController {

    private final RoutineLikeService routineLikeService;

    @PostMapping("/{routineId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void likeRoutine(@PathVariable UUID routineId) {
        routineLikeService.addLikeToRoutine(routineId);
    }

    @DeleteMapping("/{routineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikeRoutine(@PathVariable UUID routineId) {
        routineLikeService.removeLikeFromRoutine(routineId);
    }
}
