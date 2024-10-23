package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineExerciseService {
    private final RoutineRepository routineRepository;

    @Transactional
    public void updateExercisesInRoutine() {

    }

    @Transactional
    public void removeExerciseFromRoutine() {

    }
}
