package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.ExerciseDetailDto;
import com.sourcery.gymapp.backend.workout.exception.ExerciseNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.ExerciseMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private static final int DEFAULT_EXERCISE_LIMIT = 10;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public Map<UUID, Exercise> getExerciseMapByIds(List<UUID> exerciseIds) {
        List<Exercise> exercises = exerciseRepository.findAllByIdIn(exerciseIds);

        Map<UUID, Exercise> exerciseMap =
                exercises.stream().collect(Collectors.toMap(Exercise::getId, exercise -> exercise));

        Set<UUID> missingIds = new HashSet<>(exerciseIds);
        missingIds.removeAll(exerciseMap.keySet());

        if (!missingIds.isEmpty()) {
            throw new ExerciseNotFoundException(missingIds);
        }

        return exerciseMap;
    }

    public List<ExerciseDetailDto> getExercisesByPrefix(String prefix, Integer limit) {
        int effectiveLimit = (limit != null) ? limit : DEFAULT_EXERCISE_LIMIT;

        return exerciseRepository.findTopByPrefixOrContaining(prefix, effectiveLimit).stream()
                .map(exerciseMapper::toDto)
                .collect(Collectors.toList());
    }
}
