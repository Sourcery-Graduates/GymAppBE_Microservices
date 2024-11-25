package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.backend.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.backend.workout.factory.WorkoutExerciseFactory;
import com.sourcery.gymapp.backend.workout.factory.WorkoutExerciseSetFactory;
import com.sourcery.gymapp.backend.workout.mapper.WorkoutExerciseSetMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;
import com.sourcery.gymapp.backend.workout.model.WorkoutExerciseSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseSetServiceTest {

    @Mock
    WorkoutExerciseSetMapper workoutExerciseSetMapper;

    @InjectMocks
    private WorkoutExerciseSetService workoutExerciseSetService;

    private WorkoutExercise workoutExercise;
    private CreateWorkoutExerciseDto createWorkoutExerciseDto;
    private WorkoutExerciseSet existingWorkoutExerciseSet;
    private CreateWorkoutExerciseSetDto createWorkoutExerciseSetDto;

    @BeforeEach
    void setUp() {
        Exercise exercise = ExerciseFactory.createExercise();
        createWorkoutExerciseSetDto = WorkoutExerciseSetFactory.createCreateWorkoutExerciseSetDto();
        existingWorkoutExerciseSet = WorkoutExerciseSetFactory.createWorkoutExerciseSet();
        existingWorkoutExerciseSet.setId(createWorkoutExerciseSetDto.id());
        workoutExercise = WorkoutExerciseFactory.createWorkoutExercise(exercise);
        workoutExercise.addSet(existingWorkoutExerciseSet);
        createWorkoutExerciseDto = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(
                exercise.getId(),
                List.of(createWorkoutExerciseSetDto)
        );
    }

    @Test
    void shouldUpdateWorkoutExerciseSetSuccessfully() {
        CreateWorkoutExerciseSetDto updateWorkoutExerciseSetDto = new CreateWorkoutExerciseSetDto(
                createWorkoutExerciseSetDto.id(),
                2,
                12,
                new BigDecimal(80),
                120,
                "Updated comment"
        );
        CreateWorkoutExerciseDto updateWorkoutExerciseDto = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(
                createWorkoutExerciseDto.exerciseId(),
                List.of(updateWorkoutExerciseSetDto)
        );

        workoutExerciseSetService.updateSets(updateWorkoutExerciseDto, workoutExercise);

        assertAll(
                () -> assertEquals(1, workoutExercise.getSets().size()),
                () -> assertEquals(workoutExercise.getSets().getFirst().getId(), existingWorkoutExerciseSet.getId()),
                () -> assertEquals(workoutExercise.getSets().getFirst().getSetNumber(), updateWorkoutExerciseSetDto.setNumber()),
                () -> assertEquals(workoutExercise.getSets().getFirst().getReps(), updateWorkoutExerciseSetDto.reps()),
                () -> assertEquals(workoutExercise.getSets().getFirst().getWeight(), updateWorkoutExerciseSetDto.weight()),
                () -> assertEquals(workoutExercise.getSets().getFirst().getRestTime(), updateWorkoutExerciseSetDto.restTime()),
                () -> assertEquals(workoutExercise.getSets().getFirst().getComment(), updateWorkoutExerciseSetDto.comment()),
                () -> assertEquals(workoutExercise.getSets().getFirst().getWorkoutExercise(), workoutExercise)
        );
    }

    @Test
    void shouldAddNewWorkoutExerciseSetSuccessfully() {
        WorkoutExerciseSet newWorkoutExerciseSet = WorkoutExerciseSetFactory.createWorkoutExerciseSet();
        CreateWorkoutExerciseSetDto newWorkoutExerciseSetDto = new CreateWorkoutExerciseSetDto(
                newWorkoutExerciseSet.getId(),
                2,
                12,
                new BigDecimal(80),
                120,
                "New comment"
        );
        CreateWorkoutExerciseDto updateWorkoutExerciseDto = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(
                createWorkoutExerciseDto.exerciseId(),
                List.of(createWorkoutExerciseSetDto, newWorkoutExerciseSetDto)
        );
        when(workoutExerciseSetMapper.toEntity(newWorkoutExerciseSetDto, workoutExercise)).thenReturn(newWorkoutExerciseSet);

        workoutExerciseSetService.updateSets(updateWorkoutExerciseDto, workoutExercise);

        assertEquals(2, workoutExercise.getSets().size());
        verify(workoutExerciseSetMapper).toEntity(newWorkoutExerciseSetDto, workoutExercise);
    }

    @Test
    void shouldRemoveWorkoutExerciseSetSuccessfully() {
        CreateWorkoutExerciseDto updateWorkoutExerciseDto = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(
                createWorkoutExerciseDto.exerciseId(),
                List.of()
        );

        workoutExerciseSetService.updateSets(updateWorkoutExerciseDto, workoutExercise);

        assertTrue(workoutExercise.getSets().isEmpty());
        verify(workoutExerciseSetMapper, never()).toEntity(any(), any());
    }

    @Test
    void shouldHandleRemovingAndAddingAtTheSameTimeSuccessfully() {
        WorkoutExerciseSet newWorkoutExerciseSet = WorkoutExerciseSetFactory.createWorkoutExerciseSet();
        CreateWorkoutExerciseSetDto newWorkoutExerciseSetDto = new CreateWorkoutExerciseSetDto(
                newWorkoutExerciseSet.getId(),
                2,
                12,
                new BigDecimal(80),
                120,
                "New comment"
        );
        CreateWorkoutExerciseDto updateWorkoutExerciseDto = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(
                createWorkoutExerciseDto.exerciseId(),
                List.of(newWorkoutExerciseSetDto)
        );
        when(workoutExerciseSetMapper.toEntity(newWorkoutExerciseSetDto, workoutExercise)).thenReturn(newWorkoutExerciseSet);

        workoutExerciseSetService.updateSets(updateWorkoutExerciseDto, workoutExercise);

        assertAll(
                () -> assertEquals(1, workoutExercise.getSets().size()),
                () -> assertEquals(workoutExercise.getSets().getFirst().getId(), newWorkoutExerciseSet.getId()),
                () -> assertEquals(workoutExercise.getSets().getFirst().getWorkoutExercise(), workoutExercise)
        );
        verify(workoutExerciseSetMapper).toEntity(newWorkoutExerciseSetDto, workoutExercise);
    }
}
