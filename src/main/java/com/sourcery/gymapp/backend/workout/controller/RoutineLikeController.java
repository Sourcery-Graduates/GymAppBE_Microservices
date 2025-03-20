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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout/routine/like")
public class RoutineLikeController {

    private final RoutineLikeService routineLikeService;

    @Operation(
        summary = "Like a routine",
        description = "Adds a like to the specified workout routine for the current user"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Like successfully added"),
        @ApiResponse(responseCode = "409", description = "Like already exists"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Routine not found")
    })
    @PostMapping("/{routineId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void likeRoutine(@PathVariable UUID routineId) {
        routineLikeService.addLikeToRoutine(routineId);
    }

    @Operation(
        summary = "Unlike a routine",
        description = "Removes the current user's like from the specified workout routine"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Like successfully removed"),
        @ApiResponse(responseCode = "404", description = "Like not found - user hasn't liked this routine"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "Routine not found")
    })
    @DeleteMapping("/{routineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikeRoutine(@PathVariable UUID routineId) {
        routineLikeService.removeLikeFromRoutine(routineId);
    }
}
