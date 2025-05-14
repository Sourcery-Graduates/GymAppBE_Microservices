package com.sourcery.gymapp.workout.mapper;

import com.sourcery.gymapp.workout.dto.CreateRoutineExerciseDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineExerciseDto;
import com.sourcery.gymapp.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.workout.factory.RoutineFactory;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.model.RoutineExercise;
import com.sourcery.gymapp.workout.mapper.RoutineExerciseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RoutineExerciseMapperTest {

    private RoutineExerciseMapper routineExerciseMapper;

    private Routine routine;
    private Exercise exercise;
    private RoutineExercise routineExercise;
    private CreateRoutineExerciseDto createRoutineExerciseDto;

    @BeforeEach
    void setUp() {
        routineExerciseMapper = new RoutineExerciseMapper();
        routine = RoutineFactory.createRoutine();
        exercise = ExerciseFactory.createExercise();
        routineExercise = ExerciseFactory.createRoutineExercise(routine, exercise);
        createRoutineExerciseDto = ExerciseFactory.createRoutineExerciseDto();
    }

    @Test
    void shouldMapCreateRoutineExerciseDtoToRoutineExerciseEntity() {
        // Act
        RoutineExercise result = routineExerciseMapper.toEntity(createRoutineExerciseDto, routine, exercise);

        // Assert
        assertAll(
                () -> assertEquals(createRoutineExerciseDto.exerciseId(), result.getId()),
                () -> assertEquals(routine, result.getRoutine()),
                () -> assertEquals(exercise, result.getExercise()),
                () -> assertEquals(createRoutineExerciseDto.orderNumber(), result.getOrderNumber()),
                () -> assertEquals(createRoutineExerciseDto.defaultSets(), result.getDefaultSets()),
                () -> assertEquals(createRoutineExerciseDto.defaultReps(), result.getDefaultReps()),
                () -> assertEquals(createRoutineExerciseDto.defaultWeight(), result.getDefaultWeight()),
                () -> assertEquals(createRoutineExerciseDto.defaultRestTime(), result.getDefaultRestTime())
        );
    }

    @Test
    void shouldMapRoutineExerciseToCreateRoutineExerciseDto() {
        // Act
        CreateRoutineExerciseDto result = routineExerciseMapper.toCreateRoutineExerciseDto(routineExercise);

        // Assert
        assertAll(
                () -> assertEquals(routineExercise.getId(), result.exerciseId()),
                () -> assertEquals(routineExercise.getOrderNumber(), result.orderNumber()),
                () -> assertEquals(routineExercise.getDefaultSets(), result.defaultSets()),
                () -> assertEquals(routineExercise.getDefaultReps(), result.defaultReps()),
                () -> assertEquals(routineExercise.getDefaultWeight(), result.defaultWeight()),
                () -> assertEquals(routineExercise.getDefaultRestTime(), result.defaultRestTime()),
                () -> assertEquals(routineExercise.getNotes(), result.notes())
        );
    }

    @Test
    void shouldMapRoutineExerciseToResponseRoutineExerciseDto() {
        // Act
        ResponseRoutineExerciseDto result = routineExerciseMapper.toResponseRoutineExerciseDto(routineExercise);

        // Assert
        assertAll(
                () -> assertEquals(routineExercise.getId(), result.routineExerciseId()),
                () -> assertEquals(routineExercise.getOrderNumber(), result.orderNumber()),
                () -> assertEquals(routineExercise.getDefaultSets(), result.defaultSets()),
                () -> assertEquals(routineExercise.getDefaultReps(), result.defaultReps()),
                () -> assertEquals(routineExercise.getDefaultWeight(), result.defaultWeight()),
                () -> assertEquals(routineExercise.getDefaultRestTime(), result.defaultRestTime()),
                () -> assertEquals(routineExercise.getNotes(), result.notes()),
                () -> assertEquals(routineExercise.getExercise().getId(), result.exercise().id()),
                () -> assertEquals(routineExercise.getExercise().getName(), result.exercise().name())
        );
    }
}
