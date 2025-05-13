package com.sourcery.gymapp.workout.factory;


import com.sourcery.gymapp.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.workout.model.Routine;

import java.time.LocalDateTime;
import java.util.UUID;

public class RoutineFactory {

    public static Routine createRoutine(String name, String description, LocalDateTime createdAt, UUID userId, long likesCount) {
        Routine routine = new Routine();

        routine.setId(null);
        routine.setName(name);
        routine.setDescription(description);
        routine.setCreatedAt(createdAt);
        routine.setUserId(userId);
        routine.setLikesCount(likesCount);
        return routine;
    }

    public static Routine createRoutine(String name, UUID id, UUID userId) {
        Routine routine = new Routine();

        routine.setId(id);
        routine.setName(name);
        routine.setDescription("Test Description");
        routine.setUserId(userId);
        routine.setLikesCount(0L);
        return routine;
    }

    public static Routine createRoutine(String name, String description) {
        return createRoutine(
                name,
                description,
                LocalDateTime.now(),
                null,
                0L
        );
    }

    public static Routine createRoutine(String name) {
        return createRoutine(
                name,
                "Test Description",
                LocalDateTime.now(),
                null,
                0L
        );
    }

    public static Routine createRoutine() {
        return createRoutine(
                "Test Routine",
                "Test Description",
                LocalDateTime.now(),
                null,
                0L
        );
    }

    public static CreateRoutineDto createRoutineDto(String name, String description) {
        return new CreateRoutineDto(
                name,
                description
        );
    }

    public static CreateRoutineDto createRoutineDto() {
        return createRoutineDto(
                "Test Routine",
                "Test Description"
        );
    }

    public static ResponseRoutineDto createResponseRoutineDto(UUID id, String name, String description, LocalDateTime createdAt, UUID userId, long likesCount, boolean isLikedByCurrentUser) {
        return new ResponseRoutineDto(
                id,
                name,
                description,
                createdAt,
                userId,
                likesCount,
                isLikedByCurrentUser

        );
    }

    public static ResponseRoutineDto createResponseRoutineDto() {
        return createResponseRoutineDto(
                null,
                "Test Routine",
                "Test Description",
                LocalDateTime.now(),
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                0L,
                true
        );
    }
}
