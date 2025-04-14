package com.sourcery.gymapp.workout.controller;
import com.sourcery.gymapp.backend.workout.dto.ExercisePageDto;
import com.sourcery.gymapp.backend.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.backend.workout.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ExerciseControllerTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseController exerciseController;

    private ExercisePageDto exercisePageDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exercisePageDto = new ExercisePageDto(1, 1L,
                List.of(ExerciseFactory.createExerciseDetailDto())
        );
    }

    @Test
    void shouldReturnExercisesByPrefixWithPagination() {
        // Arrange
        when(exerciseService.getExercisesByPrefix(anyString(), any(Pageable.class)))
                .thenReturn(exercisePageDto);
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        ExercisePageDto result = exerciseController.getPagedExercises(pageable, "Test");

        // Assert
        assertEquals(1, result.totalElements());
        assertEquals(1, result.totalPages());
        assertEquals("Test Exercise", result.data().get(0).name());
    }

    @Test
    void shouldReturnDefaultPageWhenNoPageableProvided() {
        // Arrange
        when(exerciseService.getExercisesByPrefix(anyString(), any(Pageable.class)))
                .thenReturn(exercisePageDto);

        // Act
        ExercisePageDto result =
                exerciseController.getPagedExercises(PageRequest.of(0, 10), "Test");

        // Assert
        assertEquals(1, result.totalElements());
        assertEquals(1, result.totalPages());
    }
}
