package com.sourcery.gymapp.backend.workout.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoutineExerciseTest {

    @Mock
    private RoutineExerciseService routineExerciseService;


    private RoutineExercise routineExercise;
    private Routine routine;
    private Exercise exercise;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void shouldAddExerciseToRoutine() {

    }
}
