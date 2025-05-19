package com.sourcery.gymapp.workout.mapper;

import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.dto.ResponseWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.workout.factory.WorkoutExerciseFactory;
import com.sourcery.gymapp.workout.factory.WorkoutExerciseSetFactory;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.WorkoutExercise;
import com.sourcery.gymapp.workout.model.WorkoutExerciseSet;
import com.sourcery.gymapp.workout.mapper.WorkoutExerciseSetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseSetMapperTest {

    private WorkoutExerciseSetMapper workoutExerciseSetMapper;

    @BeforeEach
    void setUp() {
        workoutExerciseSetMapper = new WorkoutExerciseSetMapper();
    }

    @Test
    void shouldMapWorkoutExerciseSetToResponseWorkoutExerciseSetDto() {
        WorkoutExerciseSet workoutExerciseSet = WorkoutExerciseSetFactory.createWorkoutExerciseSet();

        ResponseWorkoutExerciseSetDto result = workoutExerciseSetMapper.toDto(workoutExerciseSet);

        assertAll(
                () -> assertEquals(workoutExerciseSet.getId(), result.id()),
                () -> assertEquals(workoutExerciseSet.getSetNumber(), result.setNumber()),
                () -> assertEquals(workoutExerciseSet.getReps(), result.reps()),
                () -> assertEquals(workoutExerciseSet.getWeight(), result.weight()),
                () -> assertEquals(workoutExerciseSet.getRestTime(), result.restTime()),
                () -> assertEquals(workoutExerciseSet.getComment(), result.comment())
        );
    }

    @Test
    void shouldMapCreateWorkoutExerciseSetDtoToWorkoutExerciseSet() {
        CreateWorkoutExerciseSetDto createWorkoutExerciseSetDto = WorkoutExerciseSetFactory.createCreateWorkoutExerciseSetDto();
        Exercise exercise = ExerciseFactory.createExercise();
        WorkoutExercise workoutExercise = WorkoutExerciseFactory.createWorkoutExercise(exercise);

        WorkoutExerciseSet result = workoutExerciseSetMapper.toEntity(createWorkoutExerciseSetDto, workoutExercise);

        assertAll(
                () -> assertEquals(createWorkoutExerciseSetDto.id(), result.getId()),
                () -> assertEquals(createWorkoutExerciseSetDto.setNumber(), result.getSetNumber()),
                () -> assertEquals(createWorkoutExerciseSetDto.reps(), result.getReps()),
                () -> assertEquals(createWorkoutExerciseSetDto.weight(), result.getWeight()),
                () -> assertEquals(createWorkoutExerciseSetDto.restTime(), result.getRestTime()),
                () -> assertEquals(createWorkoutExerciseSetDto.comment(), result.getComment()),
                () -> assertEquals(workoutExercise, result.getWorkoutExercise())
        );
    }

}
