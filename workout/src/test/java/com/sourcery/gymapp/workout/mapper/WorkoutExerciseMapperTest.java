package com.sourcery.gymapp.workout.mapper;

import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.dto.ResponseWorkoutExerciseDto;
import com.sourcery.gymapp.workout.dto.ResponseWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.workout.factory.WorkoutExerciseFactory;
import com.sourcery.gymapp.workout.factory.WorkoutExerciseSetFactory;
import com.sourcery.gymapp.workout.factory.WorkoutFactory;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.Workout;
import com.sourcery.gymapp.workout.model.WorkoutExercise;
import com.sourcery.gymapp.workout.model.WorkoutExerciseSet;
import com.sourcery.gymapp.workout.mapper.WorkoutExerciseMapper;
import com.sourcery.gymapp.workout.mapper.WorkoutExerciseSetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseMapperTest {

    @Mock
    private WorkoutExerciseSetMapper workoutExerciseSetMapper;

    @InjectMocks
    private WorkoutExerciseMapper workoutExerciseMapper;

    private WorkoutExerciseSet workoutExerciseSet;
    private Exercise exercise;

    @BeforeEach
    void setUp() {
        exercise = ExerciseFactory.createExercise();
        workoutExerciseSet = WorkoutExerciseSetFactory.createWorkoutExerciseSet();
    }

    @Test
    void shouldMapWorkoutExerciseToWorkoutExerciseDto() {
        WorkoutExercise workoutExercise = WorkoutExerciseFactory.createWorkoutExercise(exercise);
        workoutExercise.addSet(workoutExerciseSet);
        ResponseWorkoutExerciseSetDto responseWorkoutExerciseSetDto = WorkoutExerciseSetFactory.createResponseWorkoutExerciseSetDto();
        when(workoutExerciseSetMapper.toDto(workoutExerciseSet)).thenReturn(responseWorkoutExerciseSetDto);

        ResponseWorkoutExerciseDto result = workoutExerciseMapper.toDto(workoutExercise);

        assertAll(
                () -> assertEquals(result.id(), workoutExercise.getId()),
                () -> assertEquals(result.exercise().id(), workoutExercise.getExercise().getId()),
                () -> assertEquals(result.exercise().name(), workoutExercise.getExercise().getName()),
                () -> assertEquals(result.orderNumber(), workoutExercise.getOrderNumber()),
                () -> assertEquals(result.notes(), workoutExercise.getNotes()),
                () -> assertEquals(result.sets(), List.of(responseWorkoutExerciseSetDto))
        );
    }

    @Test
    void shouldMapWorkoutExerciseDtoToWorkoutExercise() {
        CreateWorkoutExerciseSetDto createWorkoutExerciseSetDto = WorkoutExerciseSetFactory.createCreateWorkoutExerciseSetDto();
        CreateWorkoutExerciseDto createWorkoutExerciseDto = WorkoutExerciseFactory
                .createCreateWorkoutExerciseDto(exercise.getId(), List.of(createWorkoutExerciseSetDto));
        Workout workout = WorkoutFactory.createWorkout();
        when(workoutExerciseSetMapper.toEntity(eq(createWorkoutExerciseSetDto), any(WorkoutExercise.class))).thenReturn(workoutExerciseSet);

        WorkoutExercise result = workoutExerciseMapper.toEntity(createWorkoutExerciseDto, exercise, workout);

        assertAll(
                () -> assertEquals(result.getId(), createWorkoutExerciseDto.id()),
                () -> assertEquals(result.getOrderNumber(), createWorkoutExerciseDto.orderNumber()),
                () -> assertEquals(result.getNotes(), createWorkoutExerciseDto.notes()),
                () -> assertEquals(result.getExercise(), exercise),
                () -> assertEquals(result.getWorkout(), workout),
                () -> assertEquals(result.getSets(), List.of(workoutExerciseSet))
        );
        verify(workoutExerciseSetMapper).toEntity(eq(createWorkoutExerciseSetDto), any(WorkoutExercise.class));
    }
}
