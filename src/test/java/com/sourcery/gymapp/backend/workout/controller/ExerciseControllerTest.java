package com.sourcery.gymapp.backend.workout.controller;

import com.sourcery.gymapp.backend.workout.dto.ExerciseDetailDto;
import com.sourcery.gymapp.backend.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.backend.workout.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ExerciseControllerTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseController exerciseController;

    private List<ExerciseDetailDto> exerciseDtos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exerciseDtos = List.of(ExerciseFactory.createExerciseDetailDto());
    }

    @Test
    void shouldReturnExercisesByPrefix() {
        // Arrange
        when(exerciseService.getExercisesByPrefix(anyString(), anyInt())).thenReturn(exerciseDtos);

        // Act
        List<ExerciseDetailDto> result = exerciseController.getExercisesByPrefix("Test", 10);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Exercise", result.getFirst().name());
    }

    @Test
    void shouldReturnDefaultLimitWhenLimitIsNotProvided() {
        // Arrange
        when(exerciseService.getExercisesByPrefix(anyString(), eq(null))).thenReturn(exerciseDtos);

        // Act
        List<ExerciseDetailDto> result = exerciseController.getExercisesByPrefix("Test", null);

        // Assert
        assertEquals(1, result.size());
    }
}
