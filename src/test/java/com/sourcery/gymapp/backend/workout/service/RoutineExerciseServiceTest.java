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
import java.util.Map;
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
    private List<CreateRoutineExerciseDto> createRoutineExercisesDto;
    private Map<UUID, Exercise> exerciseMap;
    private List<RoutineExercise> routineExercises;

    @BeforeEach
    void setup() {
        routine = RoutineFactory.createRoutine();
        routineId = routine.getId();

        CreateRoutineExerciseDto exerciseDto = ExerciseFactory.createRoutineExerciseDto();
        createRoutineExercisesDto = List.of(exerciseDto);

        Exercise exercise = new Exercise();
        exercise.setId(exerciseDto.exerciseId());
        exerciseMap = Map.of(exerciseDto.exerciseId(), exercise);

        RoutineExercise routineExercise = ExerciseFactory.createRoutineExercise(routine, exercise);

        routineExercises = List.of(routineExercise);
    }

    @Test
    void shouldUpdateRoutineExercises() {
        // Arrange
        when(routineService.findRoutineById(routineId)).thenReturn(routine);
        when(exerciseService.getExerciseMapByIds(anyList())).thenReturn(exerciseMap);
        when(routineExerciseMapper.toEntity(any(CreateRoutineExerciseDto.class), eq(routine), any(Exercise.class)))
                .thenReturn(routineExercises.getFirst());
        when(routineExerciseMapper.toResponseRoutineExerciseDto(any(RoutineExercise.class)))
                .thenReturn(ExerciseFactory.createResponseRoutineExerciseDto());

        // Act
        ResponseRoutineListExerciseDto result = routineExerciseService.replaceExercisesInRoutine(routineId, createRoutineExercisesDto);

        // Assert
        verify(routineExerciseRepository).deleteAllByRoutineId(routineId);
        verify(routineExerciseRepository).saveAll(anyList());
        assertEquals(routineId, result.routineId());
        assertEquals(1, result.exercises().size());
    }

    @Test
    void shouldGetRoutineExercises() {
        // Arrange
        when(routineService.findRoutineById(routineId)).thenReturn(routine);
        when(routineExerciseRepository.findAllByRoutineId(routineId)).thenReturn(routineExercises);
        when(routineExerciseMapper.toResponseRoutineExerciseDto(any(RoutineExercise.class)))
                .thenReturn(ExerciseFactory.createResponseRoutineExerciseDto());

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
        when(routineService.findRoutineById(routineId)).thenReturn(routine);
        when(routineExerciseRepository.findAllByRoutineId(routineId)).thenReturn(List.of());

        // Act
        ResponseRoutineListExerciseDto result = routineExerciseService.getExercisesFromRoutine(routineId);

        // Assert
        assertTrue(result.exercises().isEmpty());
        assertEquals(routineId, result.routineId());
    }
}
