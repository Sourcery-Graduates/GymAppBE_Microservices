package com.sourcery.gymapp.workout.validation;

import com.sourcery.gymapp.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.exception.WrongOrderException;
import com.sourcery.gymapp.workout.factory.WorkoutExerciseFactory;
import com.sourcery.gymapp.workout.factory.WorkoutExerciseSetFactory;
import com.sourcery.gymapp.workout.factory.WorkoutFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private ValidationAspect validationAspect;

    @Test
    void shouldProceedWhenValidOrdersAreProvided() throws Throwable {
        CreateWorkoutExerciseSetDto set1 = WorkoutExerciseSetFactory.createCreateWorkoutExerciseSetDto(1);
        CreateWorkoutExerciseSetDto set2 = WorkoutExerciseSetFactory.createCreateWorkoutExerciseSetDto(2);
        CreateWorkoutExerciseDto exercise1 = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(1, List.of(set1, set2));
        CreateWorkoutExerciseDto exercise2 = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(2, List.of(set1, set2));
        CreateWorkoutDto workoutDto = WorkoutFactory.createCreateWorkoutDto(null, null, List.of(exercise1, exercise2));

        when(joinPoint.getArgs()).thenReturn(new Object[]{workoutDto});
        when(joinPoint.proceed()).thenReturn("Success");

        Object result = validationAspect.validateOrder(joinPoint);

        assertEquals("Success", result);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotStartFromOne() {
        CreateWorkoutExerciseSetDto set1 = WorkoutExerciseSetFactory.createCreateWorkoutExerciseSetDto(2);
        CreateWorkoutExerciseDto exercise = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(1, List.of(set1));
        CreateWorkoutDto workoutDto = WorkoutFactory.createCreateWorkoutDto(null, null, List.of(exercise));

        when(joinPoint.getArgs()).thenReturn(new Object[]{workoutDto});

        assertThrows(WrongOrderException.class, () -> validationAspect.validateOrder(joinPoint));
    }

    @Test
    void shouldThrowExceptionWhenOrderIsNotSequential() {
        CreateWorkoutExerciseSetDto set1 = WorkoutExerciseSetFactory.createCreateWorkoutExerciseSetDto(1);
        CreateWorkoutExerciseSetDto set3 = WorkoutExerciseSetFactory.createCreateWorkoutExerciseSetDto(3);
        CreateWorkoutExerciseDto exercise = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(1, List.of(set1, set3));
        CreateWorkoutDto workoutDto = WorkoutFactory.createCreateWorkoutDto(null, null, List.of(exercise));

        when(joinPoint.getArgs()).thenReturn(new Object[]{workoutDto});

        assertThrows(WrongOrderException.class, () -> validationAspect.validateOrder(joinPoint));
    }

    @Test
    void shouldProceedSuccessfullyWhenNoExercisesArePresent() throws Throwable {
        CreateWorkoutDto workoutDto = WorkoutFactory.createCreateWorkoutDto(null, null, null);

        when(joinPoint.getArgs()).thenReturn(new Object[]{workoutDto});
        when(joinPoint.proceed()).thenReturn("Success");

        Object result = validationAspect.validateOrder(joinPoint);

        assertEquals("Success", result);
    }

    @Test
    void shouldProceedSuccessfullyWhenExercisesHaveEmptySets() throws Throwable {
        CreateWorkoutExerciseDto exercise = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(1, null);
        CreateWorkoutDto workoutDto = WorkoutFactory.createCreateWorkoutDto(null, null, List.of(exercise));

        when(joinPoint.getArgs()).thenReturn(new Object[]{workoutDto});
        when(joinPoint.proceed()).thenReturn("Success");

        Object result = validationAspect.validateOrder(joinPoint);

        assertEquals("Success", result);
    }

    @Test
    void shouldThrowExceptionWhenExercisesAreNotInOrder() {
        CreateWorkoutExerciseDto exercise1 = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(1, null);
        CreateWorkoutExerciseDto exercise3 = WorkoutExerciseFactory.createCreateWorkoutExerciseDto(3, null);
        CreateWorkoutDto workoutDto = WorkoutFactory.createCreateWorkoutDto(null, null, List.of(exercise1, exercise3));

        when(joinPoint.getArgs()).thenReturn(new Object[]{workoutDto});

        assertThrows(WrongOrderException.class, () -> validationAspect.validateOrder(joinPoint));
    }
}
