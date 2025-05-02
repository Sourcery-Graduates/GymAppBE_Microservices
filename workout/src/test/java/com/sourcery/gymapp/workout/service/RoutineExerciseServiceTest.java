package com.sourcery.gymapp.workout.service;

import com.sourcery.gymapp.workout.dto.*;
import com.sourcery.gymapp.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.workout.factory.RoutineFactory;
import com.sourcery.gymapp.workout.mapper.RoutineExerciseMapper;
import com.sourcery.gymapp.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.model.RoutineExercise;
import com.sourcery.gymapp.workout.repository.RoutineExerciseRepository;
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
    @Mock
    private RoutineMapper routineMapper;

    @InjectMocks
    private RoutineExerciseService routineExerciseService;


    private Routine routine;
    private Exercise exercise;
    private ExerciseSimpleDto exerciseSimpleDto;
    private ResponseRoutineDto responseRoutineDto;
    private CreateRoutineExerciseDto createRoutineExerciseDto;
    private List<CreateRoutineExerciseDto> createRoutineExerciseListDto;
    private ResponseRoutineExerciseDto responseRoutineExerciseDto;
    private Map<UUID, Exercise> exerciseMap;
    private List<RoutineExercise> routineExercises;
    private UUID routineId;

    @BeforeEach
    void setup() {
        routine = RoutineFactory.createRoutine();
        routineId = routine.getId();

        responseRoutineDto = RoutineFactory.createResponseRoutineDto(
                routine.getId(),
                routine.getName(),
                routine.getDescription(),
                routine.getCreatedAt(),
                routine.getUserId(),
                routine.getLikesCount(),
                true
        );

        createRoutineExerciseDto = ExerciseFactory.createRoutineExerciseDto();
        createRoutineExerciseListDto = List.of(createRoutineExerciseDto);

        exercise = ExerciseFactory.createExercise();
        exercise.setId(createRoutineExerciseDto.exerciseId());
        exerciseMap = Map.of(createRoutineExerciseDto.exerciseId(), exercise);

        exerciseSimpleDto = ExerciseFactory.createExerciseSimpleDto(exercise.getId(), exercise.getName());

        RoutineExercise routineExercise = ExerciseFactory.createRoutineExercise(routine, exercise);
        routineExercises = List.of(routineExercise);

        responseRoutineExerciseDto = ExerciseFactory
                .createResponseRoutineExerciseDto(exerciseSimpleDto);
    }

    @Test
    void shouldUpdateRoutineExercises() {
        // Arrange
        when(routineService.findRoutineById(routineId)).thenReturn(routine);
        when(exerciseService.getExerciseMapByIds(anyList())).thenReturn(exerciseMap);
        when(routineExerciseMapper.toEntity(createRoutineExerciseDto, routine, exercise))
                .thenReturn(routineExercises.getFirst());
        when(routineExerciseMapper.toResponseRoutineExerciseDto(any(RoutineExercise.class)))
                .thenReturn(responseRoutineExerciseDto);
        when(routineMapper.toDto(routine)).thenReturn(responseRoutineDto);

        // Act
        ResponseRoutineDetailDto result = routineExerciseService
                .replaceExercisesInRoutine(routineId, createRoutineExerciseListDto);

        // Assert
        verify(routineExerciseRepository).deleteAllByRoutineId(routineId);
        verify(routineExerciseRepository).saveAll(anyList());
        assertAll(
                () -> assertEquals(routineId, result.routine().id()),
                () -> assertEquals(routine.getName(), result.routine().name()),
                () -> assertEquals(routine.getDescription(), result.routine().description()),
                () -> assertEquals(routine.getCreatedAt(), result.routine().createdAt()),
                () -> assertEquals(1, result.exercises().size()),
                () -> assertEquals(responseRoutineExerciseDto, result.exercises().getFirst())
        );
    }

    @Test
    void shouldGetRoutineExercises() {
        // Arrange
        when(routineService.findRoutineById(routineId)).thenReturn(routine);
        when(routineExerciseRepository.findAllByRoutineId(routineId)).thenReturn(routineExercises);
        when(routineExerciseMapper.toResponseRoutineExerciseDto(any(RoutineExercise.class)))
                .thenReturn(responseRoutineExerciseDto);
        when(routineMapper.toDto(routine)).thenReturn(responseRoutineDto);

        // Act
        ResponseRoutineDetailDto result = routineExerciseService.getRoutineDetails(routineId);

        // Assert
        verify(routineExerciseRepository).findAllByRoutineId(routineId);
        assertAll(
                () -> assertEquals(routineId, result.routine().id()),
                () -> assertEquals(routine.getName(), result.routine().name()),
                () -> assertEquals(routine.getDescription(), result.routine().description()),
                () -> assertEquals(routine.getCreatedAt(), result.routine().createdAt()),
                () -> assertEquals(1, result.exercises().size()),
                () -> assertEquals(responseRoutineExerciseDto, result.exercises().getFirst())
        );
    }

    @Test
    void shouldReturnEmptyExercises() {
        // Arrange
        when(routineService.findRoutineById(routineId)).thenReturn(routine);
        when(routineExerciseRepository.findAllByRoutineId(routineId)).thenReturn(List.of());
        when(routineMapper.toDto(routine)).thenReturn(responseRoutineDto);

        // Act
        ResponseRoutineDetailDto result = routineExerciseService.getRoutineDetails(routineId);

        // Assert
        assertAll(
                () -> assertEquals(routineId, result.routine().id()),
                () -> assertEquals(routine.getName(), result.routine().name()),
                () -> assertEquals(routine.getDescription(), result.routine().description()),
                () -> assertEquals(routine.getCreatedAt(), result.routine().createdAt()),
                () -> assertTrue(result.exercises().isEmpty())
        );
    }
}
