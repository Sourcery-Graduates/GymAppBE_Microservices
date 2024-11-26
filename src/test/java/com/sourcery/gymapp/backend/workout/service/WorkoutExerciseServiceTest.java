package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.exception.ExerciseNotFoundException;
import com.sourcery.gymapp.backend.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.backend.workout.factory.WorkoutExerciseFactory;
import com.sourcery.gymapp.backend.workout.factory.WorkoutFactory;
import com.sourcery.gymapp.backend.workout.mapper.WorkoutExerciseMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Workout;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;
import com.sourcery.gymapp.backend.workout.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseServiceTest {

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private WorkoutExerciseSetService workoutExerciseSetService;

    @Mock
    private WorkoutExerciseMapper workoutExerciseMapper;

    @InjectMocks
    private WorkoutExerciseService workoutExerciseService;

    private Workout workout;
    private CreateWorkoutDto updateWorkoutDto;
    private WorkoutExercise existingWorkoutExercise;
    private CreateWorkoutExerciseDto createWorkoutExerciseDto;

    @BeforeEach
    public void setup() {
        Exercise exercise = ExerciseFactory.createExercise();
        workout = WorkoutFactory.createWorkout();
        createWorkoutExerciseDto = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(exercise.getId());
        existingWorkoutExercise = WorkoutExerciseFactory.createWorkoutExercise(exercise);
        existingWorkoutExercise.setId(createWorkoutExerciseDto.id());
        workout.addExercise(existingWorkoutExercise);
        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                null,
                null,
                List.of(createWorkoutExerciseDto)
        );
    }

    @Test
    void shouldUpdateWorkoutExercisesSuccessfully() {
        Exercise newExercise = ExerciseFactory.createExercise();
        CreateWorkoutExerciseDto updateWorkoutExerciseDto = new CreateWorkoutExerciseDto(
                createWorkoutExerciseDto.id(), newExercise.getId(), 2, "New Notes", null
        );
        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                null,
                null,
                List.of(updateWorkoutExerciseDto)
        );

        when(exerciseService.findExerciseById(newExercise.getId()))
                .thenReturn(newExercise);

        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);

        assertAll(
                () -> assertEquals(1, workout.getExercises().size()),
                () -> assertEquals(workout.getExercises().getFirst().getId(), existingWorkoutExercise.getId()),
                () -> assertEquals(workout.getExercises().getFirst().getExercise(), newExercise),
                () -> assertEquals(workout.getExercises().getFirst().getOrderNumber(), updateWorkoutExerciseDto.orderNumber()),
                () -> assertEquals(workout.getExercises().getFirst().getNotes(), updateWorkoutExerciseDto.notes())
        );
        verify(workoutExerciseSetService, times(1)).updateSets(updateWorkoutExerciseDto, existingWorkoutExercise);
        verify(exerciseService).findExerciseById(newExercise.getId());

    }

    @Test
    void shouldAddNewWorkoutExercisesSuccessfully() {
        Exercise newExercise = ExerciseFactory.createExercise();
        WorkoutExercise newWorkoutExercise = WorkoutExerciseFactory.createWorkoutExercise(newExercise);
        CreateWorkoutExerciseDto newWorkoutExerciseDto = new CreateWorkoutExerciseDto(
                newWorkoutExercise.getId(), newExercise.getId(), 2, "New Notes", null
        );

        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                null,
                null,
                List.of(createWorkoutExerciseDto, newWorkoutExerciseDto)
        );

        when(exerciseService.findExerciseById(newWorkoutExerciseDto.exerciseId()))
                .thenReturn(newExercise);
        when(workoutExerciseMapper.toEntity(eq(newWorkoutExerciseDto), any(Exercise.class), eq(workout)))
                .thenReturn(newWorkoutExercise);

        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);

        assertEquals(2, workout.getExercises().size());
        verify(workoutExerciseSetService, times(1)).updateSets(createWorkoutExerciseDto, existingWorkoutExercise);
        verify(exerciseService).findExerciseById(newWorkoutExerciseDto.exerciseId());
        verify(workoutExerciseMapper).toEntity(eq(newWorkoutExerciseDto), any(Exercise.class), eq(workout));
    }

    @Test
    void shouldRemoveWorkoutExercisesSuccessfully() {
        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                null,
                null,
                List.of()
        );

        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);

        assertTrue(workout.getExercises().isEmpty());
        verify(workoutExerciseSetService, never()).updateSets(any(), any());
        verify(exerciseService, never()).findExerciseById(any());
    }

    @Test
    void shouldHandleRemovingAndAddingAtTheSameTimeSuccessfully() {
        Exercise newExercise = ExerciseFactory.createExercise();
        WorkoutExercise newWorkoutExercise = WorkoutExerciseFactory.createWorkoutExercise(newExercise);
        CreateWorkoutExerciseDto newWorkoutExerciseDto = new CreateWorkoutExerciseDto(
                newWorkoutExercise.getId(), newExercise.getId(), 2, "New Notes", null
        );

        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                null,
                null,
                List.of(newWorkoutExerciseDto)
        );

        when(exerciseService.findExerciseById(newWorkoutExerciseDto.exerciseId()))
                .thenReturn(newExercise);
        when(workoutExerciseMapper.toEntity(eq(newWorkoutExerciseDto), any(Exercise.class), eq(workout)))
                .thenReturn(newWorkoutExercise);

        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);

        assertEquals(1, workout.getExercises().size());
        verify(exerciseService).findExerciseById(newWorkoutExerciseDto.exerciseId());
        verify(workoutExerciseMapper).toEntity(eq(newWorkoutExerciseDto), any(Exercise.class), eq(workout));
    }
}
