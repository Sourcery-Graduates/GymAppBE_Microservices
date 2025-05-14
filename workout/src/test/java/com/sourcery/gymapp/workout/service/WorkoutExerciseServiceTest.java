package com.sourcery.gymapp.workout.service;

import com.sourcery.gymapp.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.workout.exception.ExerciseNotFoundException;
import com.sourcery.gymapp.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.workout.factory.WorkoutExerciseFactory;
import com.sourcery.gymapp.workout.factory.WorkoutFactory;
import com.sourcery.gymapp.workout.mapper.WorkoutExerciseMapper;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.Workout;
import com.sourcery.gymapp.workout.model.WorkoutExercise;
import com.sourcery.gymapp.workout.repository.ExerciseRepository;
import com.sourcery.gymapp.workout.service.ExerciseService;
import com.sourcery.gymapp.workout.service.WorkoutExerciseService;
import com.sourcery.gymapp.workout.service.WorkoutExerciseSetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

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
        Exercise exercise = ExerciseFactory.createExerciseWithRandomId();
        workout = WorkoutFactory.createWorkoutWithRandomId();
        createWorkoutExerciseDto = WorkoutExerciseFactory.createCreateWorkoutExerciseDtoWithRandomId(exercise.getId());
        existingWorkoutExercise = WorkoutExerciseFactory.createWorkoutExerciseWithRandomId(exercise);
        workout.addExercise(existingWorkoutExercise);
        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                null,
                null,
                List.of(createWorkoutExerciseDto)
        );
    }

//    @Test
//    void shouldUpdateWorkoutExercisesSuccessfully() {
//        Exercise newExercise = ExerciseFactory.createExercise();
//        CreateWorkoutExerciseDto updateWorkoutExerciseDto = new CreateWorkoutExerciseDto(
//                createWorkoutExerciseDto.id(), newExercise.getId(), 2, "New Notes", null
//        );
//        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
//                null,
//                null,
//                List.of(updateWorkoutExerciseDto)
//        );
//
//        when(exerciseService.findExerciseById(newExercise.getId()))
//                .thenReturn(newExercise);
//
//        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);
//
//        assertAll(
//                () -> assertEquals(1, workout.getExercises().size()),
//                () -> assertEquals(workout.getExercises().getFirst().getId(), existingWorkoutExercise.getId()),
//                () -> assertEquals(workout.getExercises().getFirst().getExercise(), newExercise),
//                () -> assertEquals(workout.getExercises().getFirst().getOrderNumber(), updateWorkoutExerciseDto.orderNumber()),
//                () -> assertEquals(workout.getExercises().getFirst().getNotes(), updateWorkoutExerciseDto.notes())
//        );
//        verify(workoutExerciseSetService, times(1)).updateSets(updateWorkoutExerciseDto, existingWorkoutExercise);
//        verify(exerciseService).findExerciseById(newExercise.getId());
//
//    }
//
//    @Test
//    void shouldAddNewWorkoutExercisesSuccessfully_WhenRandomIdProvided() {
//        Exercise newExercise = ExerciseFactory.createExercise();
//        WorkoutExercise newWorkoutExercise = WorkoutExerciseFactory.createWorkoutExerciseWithRandomId(newExercise);
//        CreateWorkoutExerciseDto newWorkoutExerciseDto = new CreateWorkoutExerciseDto(
//                newWorkoutExercise.getId(), newExercise.getId(), 2, "New Notes", null
//        );
//
//        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
//                null,
//                null,
//                List.of(createWorkoutExerciseDto, newWorkoutExerciseDto)
//        );
//
//        when(exerciseService.findExerciseById(newWorkoutExerciseDto.exerciseId()))
//                .thenReturn(newExercise);
//        when(workoutExerciseMapper.toEntity(eq(newWorkoutExerciseDto), any(Exercise.class), eq(workout)))
//                .thenReturn(newWorkoutExercise);
//
//        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);
//
//        assertEquals(2, workout.getExercises().size());
//        verify(workoutExerciseSetService, times(1)).updateSets(createWorkoutExerciseDto, existingWorkoutExercise);
//        verify(exerciseService).findExerciseById(newWorkoutExerciseDto.exerciseId());
//        verify(workoutExerciseMapper).toEntity(eq(newWorkoutExerciseDto), any(Exercise.class), eq(workout));
//    }
//
//    @Test
//    void shouldAddNewWorkoutExercisesSuccessfully_WhenNullIdProvided() {
//        Exercise newExercise = ExerciseFactory.createExercise();
//        WorkoutExercise newWorkoutExercise = WorkoutExerciseFactory.createWorkoutExercise(newExercise);
//        newWorkoutExercise.setId(null);
//        CreateWorkoutExerciseDto newWorkoutExerciseDto = new CreateWorkoutExerciseDto(
//                null, newExercise.getId(), 2, "New Notes", null
//        );
//
//        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
//                null,
//                null,
//                List.of(createWorkoutExerciseDto, newWorkoutExerciseDto)
//        );
//
//        when(exerciseService.findExerciseById(newWorkoutExerciseDto.exerciseId()))
//                .thenReturn(newExercise);
//        when(exerciseService.findExerciseById(createWorkoutExerciseDto.exerciseId()))
//                .thenReturn(existingWorkoutExercise.getExercise());
//        when(workoutExerciseMapper.toEntity(newWorkoutExerciseDto, newExercise, workout))
//                .thenReturn(newWorkoutExercise);
//
//        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);
//
//        assertEquals(2, workout.getExercises().size());
//        verify(workoutExerciseSetService, times(1)).updateSets(createWorkoutExerciseDto, existingWorkoutExercise);
//        verify(exerciseService).findExerciseById(newWorkoutExerciseDto.exerciseId());
//        verify(workoutExerciseMapper).toEntity(newWorkoutExerciseDto, any(Exercise.class), workout);
//    }
//
//    @Test
//    void shouldAddNewWorkoutExercisesSuccessfully_WhenProvidedTheSameExercise() {
//        WorkoutExercise newWorkoutExercise = WorkoutExerciseFactory.createWorkoutExercise(existingWorkoutExercise.getExercise());
//        CreateWorkoutExerciseDto newWorkoutExerciseDto = new CreateWorkoutExerciseDto(
//                newWorkoutExercise.getId(), existingWorkoutExercise.getExercise().getId(), 2, "New Notes", null
//        );
//
//        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
//                null,
//                null,
//                List.of(createWorkoutExerciseDto, newWorkoutExerciseDto)
//        );
//
//        when(exerciseService.findExerciseById(newWorkoutExerciseDto.exerciseId()))
//                .thenReturn(existingWorkoutExercise.getExercise());
//        when(workoutExerciseMapper.toEntity(eq(newWorkoutExerciseDto), any(Exercise.class), eq(workout)))
//                .thenReturn(newWorkoutExercise);
//
//        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);
//
//        assertEquals(2, workout.getExercises().size());
//        assertEquals(workout.getExercises().getFirst().getExercise(), workout.getExercises().getLast().getExercise());
//        verify(workoutExerciseSetService, times(1)).updateSets(createWorkoutExerciseDto, existingWorkoutExercise);
//        verify(exerciseService).findExerciseById(newWorkoutExerciseDto.exerciseId());
//        verify(workoutExerciseMapper).toEntity(eq(newWorkoutExerciseDto), any(Exercise.class), eq(workout));
//    }

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

//    @Test
//    void shouldHandleAddingAndUpdatingAtTheSameTimeSuccessfully() {
//        Exercise updateExercise = ExerciseFactory.createExerciseWithRandomId();
//        Exercise newExercise = ExerciseFactory.createExerciseWithRandomId();
//
//        CreateWorkoutExerciseDto updateWorkoutExerciseDto = new CreateWorkoutExerciseDto(
//                createWorkoutExerciseDto.id(),
//                updateExercise.getId(),
//                2,
//                "Updated Notes",
//                List.of()
//        );
//
//        WorkoutExercise newWorkoutExercise = WorkoutExerciseFactory.createWorkoutExerciseWithRandomId(newExercise);
//        CreateWorkoutExerciseDto newWorkoutExerciseDto = new CreateWorkoutExerciseDto(
//                newWorkoutExercise.getId(),
//                newExercise.getId(),
//                3,
//                "New Notes",
//                List.of()
//        );
//
//        updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
//                null,
//                null,
//                List.of(newWorkoutExerciseDto, updateWorkoutExerciseDto)
//        );
//
//        when(exerciseService.findExerciseById(updateWorkoutExerciseDto.exerciseId()))
//                .thenReturn(updateExercise);
//        when(exerciseService.findExerciseById(newWorkoutExerciseDto.exerciseId()))
//                .thenReturn(newExercise);
//        when(workoutExerciseMapper.toEntity(eq(newWorkoutExerciseDto), eq(newExercise), eq(workout)))
//                .thenReturn(newWorkoutExercise);
//
//        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);
//
//        WorkoutExercise updatedWorkoutExercise = workout.getExercises().stream()
//                .filter(e -> e.getId().equals(updateWorkoutExerciseDto.id()))
//                .findFirst()
//                .orElseThrow();
//        WorkoutExercise newlyCreatedWorkoutExercise = workout.getExercises().stream()
//                .filter(e -> e.getId().equals(newWorkoutExerciseDto.id()))
//                .findFirst()
//                .orElseThrow();
//
//        assertEquals(2, workout.getExercises().size());
//        assertAll(
//                () -> assertEquals(updateExercise, updatedWorkoutExercise.getExercise()),
//                () -> assertEquals(updateWorkoutExerciseDto.orderNumber(), updatedWorkoutExercise.getOrderNumber()),
//                () -> assertEquals(updateWorkoutExerciseDto.notes(), updatedWorkoutExercise.getNotes()),
//                () -> assertEquals(newExercise, newlyCreatedWorkoutExercise.getExercise()),
//                () -> assertEquals(newWorkoutExerciseDto.orderNumber(), newlyCreatedWorkoutExercise.getOrderNumber()),
//                () -> assertEquals(newWorkoutExerciseDto.notes(), newlyCreatedWorkoutExercise.getNotes())
//        );
//
//        verify(workoutExerciseSetService).updateSets(updateWorkoutExerciseDto, existingWorkoutExercise);
//        verify(exerciseService).findExerciseById(updateWorkoutExerciseDto.exerciseId());
//        verify(exerciseService).findExerciseById(newWorkoutExerciseDto.exerciseId());
//        verify(workoutExerciseMapper).toEntity(eq(newWorkoutExerciseDto), eq(newExercise), eq(workout));
//    }
}
