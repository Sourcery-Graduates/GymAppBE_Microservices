package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.exception.ExerciseNotFoundException;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public Set<Exercise> getAllExercisesFromDatabaseById(List<UUID> exerciseIds) {
        Set<Exercise> exercises = exerciseRepository.findAllByIdIn(exerciseIds);

        Set<UUID> foundIds = exercises
                .stream().map(Exercise::getId).collect(Collectors.toSet());

        Set<UUID> missingIds = new HashSet<>(exerciseIds);
        missingIds.removeAll(foundIds);

        if (!missingIds.isEmpty()) {
            throw new ExerciseNotFoundException(missingIds);
        }

        return exercises;
    }
}
