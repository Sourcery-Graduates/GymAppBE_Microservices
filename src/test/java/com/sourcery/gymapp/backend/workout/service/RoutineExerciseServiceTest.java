package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.*;
import com.sourcery.gymapp.backend.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.backend.workout.factory.RoutineFactory;
import com.sourcery.gymapp.backend.workout.mapper.RoutineExerciseMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.RoutineExercise;
import com.sourcery.gymapp.backend.workout.repository.RoutineExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoutineExerciseServiceTest {

    @Mock
    private RoutineService routineService;

    @Mock
    private ExerciseService exerciseService;
    @Mock
    private RoutineExerciseRepository routineExerciseRepository;
    @Mock
    private RoutineExerciseMapper routineExerciseMapper;

    @InjectMocks
    private RoutineExerciseService routineExerciseService;


    private Routine routine;
    private UUID routineId;
    private UUID userId;
    private List<CreateRoutineExerciseDto> createRoutineExercisesDto;

    @BeforeEach
    void setup() {
        routine = RoutineFactory.createRoutine();
        routineId = routine.getId();
        userId = routine.getUserId();

        ExerciseSimpleDto exerciseSimpleDto = ExerciseFactory.createExerciseSimpleDto();
        ExerciseFactory.createRoutineExercise(routine, new );
        createRoutineExercisesDto = List.of(ExerciseFactory.createRoutineExerciseDto());
    }

    @Test
    void shouldUpdateRoutineExercises() {
        // Arrange
        when(routineService.findRoutineById(routineId)).thenReturn(routine);
        when(exerciseService.getExerciseFromDatabaseById(any())).thenReturn(new Exercise());
        when(routineExerciseMapper.toEntity(any(), eq(routine), any())).thenReturn(new RoutineExercise());

        // Act
        CreateRoutineExerciseListDto result = routineExerciseService.replaceExercisesInRoutine(routineId, createRoutineExercisesDto);

        // Assert
        verify(routineExerciseRepository).deleteAllByRoutineId(routineId);
        verify(routineExerciseRepository).saveAll(anyList());
        assertEquals(routineId, result.routineId());
    }

    @Test
    void shouldGetRoutineExercises() {
        // Arrange
        List<RoutineExercise> routineExercises = List.of(new RoutineExercise());
        when(routineExerciseRepository.findAllByRoutineId(routineId)).thenReturn(routineExercises);
        when(routineExerciseMapper.toResponseRoutineExerciseDto(any())).thenReturn(new ResponseRoutineExerciseDto());

        // Act
        ResponseRoutineListExerciseDto result = routineExerciseService.getExercisesFromRoutine(routineId);

        // Assert
        assertEquals(routineId, result.routineId());
        verify(routineExerciseRepository).findAllByRoutineId(routineId);
        assertEquals(1, result.exercises().size());
    }

    @Test
    void shouldReturnEmptyExercises() {
        // Arrange
        when(routineExerciseRepository.findAllByRoutineId(routineId)).thenReturn(List.of());

        // Act
        ResponseRoutineListExerciseDto result = routineExerciseService.getExercisesFromRoutine(routineId);

        // Assert
        assertTrue(result.exercises().isEmpty());
        assertEquals(routineId, result.routineId());
    }

    @Test
    void shouldThrowValidationExceptionWhenRoutineNotFound() {
        // Arrange
        when(routineService.findRoutineById(routineId)).thenThrow(new IllegalArgumentException("Routine not found"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> routineExerciseService.getExercisesFromRoutine(routineId));
    }
}
