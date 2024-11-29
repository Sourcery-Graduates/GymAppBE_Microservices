package com.sourcery.gymapp.backend.workout.mapper;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.backend.workout.factory.RoutineFactory;
import com.sourcery.gymapp.backend.workout.factory.WorkoutExerciseFactory;
import com.sourcery.gymapp.backend.workout.factory.WorkoutFactory;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.Workout;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkoutMapperTest {

    @Mock
    private WorkoutExerciseMapper exerciseMapper;

    @InjectMocks
    private WorkoutMapper workoutMapper;

    private Workout workout;
    private Workout basedOnWorkout;
    private Routine routine;
    private WorkoutExercise workoutExercise;
    private ResponseWorkoutExerciseDto responseWorkoutExerciseDto;
    private CreateWorkoutDto createWorkoutDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        basedOnWorkout = WorkoutFactory.createWorkout();
        routine = RoutineFactory.createRoutine();
        workoutExercise = new WorkoutExercise();
        workout = WorkoutFactory.createWorkout(
                basedOnWorkout,
                routine,
                List.of(workoutExercise)
        );
        workoutExercise.setWorkout(workout);
        userId = workout.getUserId();
        responseWorkoutExerciseDto = WorkoutExerciseFactory.createResponseWorkoutExerciseDto();
    }

    @Test
    void shouldMapWorkoutToWorkoutDto() {
        when(exerciseMapper.toDto(workoutExercise)).thenReturn(responseWorkoutExerciseDto);

        ResponseWorkoutDto result = workoutMapper.toDto(workout);

        assertAll(
                () -> assertEquals(result.id(), workout.getId()),
                () -> assertEquals(result.userId(), workout.getUserId()),
                () -> assertEquals(result.name(), workout.getName()),
                () -> assertEquals(result.date(), workout.getDate()),
                () -> assertEquals(result.comment(), workout.getComment()),
                () -> assertEquals(result.basedOnWorkoutId(), workout.getBasedOnWorkout().getId()),
                () -> assertEquals(result.routineId(), workout.getRoutine().getId()),
                () -> assertEquals(result.exercises(), List.of(responseWorkoutExerciseDto))
        );
    }

    @Test
    void shouldMapWorkoutDtoToWorkout() {
        Exercise exercise = ExerciseFactory.createExercise();
        Map<UUID, Exercise> exerciseMap = Map.of(exercise.getId(), exercise);
        CreateWorkoutExerciseDto createWorkoutExerciseDto = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(exercise.getId());
        createWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                routine.getId(),
                basedOnWorkout.getId(),
                List.of(createWorkoutExerciseDto)
        );
        when(exerciseMapper.toEntity(eq(createWorkoutExerciseDto), eq(exercise), any(Workout.class))).thenReturn(workoutExercise);

        Workout result = workoutMapper.toEntity(createWorkoutDto, userId, basedOnWorkout, routine, exerciseMap);
        workoutExercise.setWorkout(result);

        assertAll(
                () -> assertEquals(result.getUserId(), userId),
                () -> assertEquals(result.getName(), createWorkoutDto.name()),
                () -> assertEquals(result.getDate(), createWorkoutDto.date()),
                () -> assertEquals(result.getComment(), createWorkoutDto.comment()),
                () -> assertEquals(result.getBasedOnWorkout(), basedOnWorkout),
                () -> assertEquals(result.getRoutine(), routine),
                () -> assertEquals(result.getExercises(), List.of(workoutExercise))
        );
    }
}
