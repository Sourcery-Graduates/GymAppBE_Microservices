package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.ExerciseDetailDto;
import com.sourcery.gymapp.backend.workout.dto.ExercisePageDto;
import com.sourcery.gymapp.backend.workout.exception.ExerciseNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.ExerciseMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.repository.ExerciseRepository;
import com.sourcery.gymapp.backend.workout.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ExercisePageDto getExercisesByPrefix(String prefix, Pageable pageable) {
        Page<Exercise> exercisePage;

        if (prefix == null || prefix.isBlank()) {
            exercisePage = exerciseRepository.findAll(pageable);
        } else {
            exercisePage = exerciseRepository.findByPrefixOrContaining(prefix, pageable);
        }

        List<ExerciseDetailDto> exercises = exercisePage.map(exerciseMapper::toDto).getContent();

        return new ExercisePageDto(
                exercisePage.getTotalPages(),
                exercisePage.getTotalElements(),
                exercises
        );
    }

    public ExercisePageDto getFilteredExercises(String prefix, String primaryMuscle, Pageable pageable) {
        Page<Exercise> exercises = fetchExercisesFromRepository(prefix, primaryMuscle, pageable);

        return new ExercisePageDto(
                exercises.getTotalPages(),
                exercises.getTotalElements(),
                exercises.getContent().stream()
                        .map(exerciseMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    public Page<Exercise> fetchExercisesFromRepository(String prefix, String primaryMuscle, Pageable pageable) {
        boolean hasPrefix = prefix != null && !prefix.isBlank();
        boolean hasPrimaryMuscle = primaryMuscle != null && !primaryMuscle.isBlank();

        if (hasPrimaryMuscle) {
            String formattedMuscles = FormatUtil.wrapInCurlyBraces(primaryMuscle);
            return hasPrefix
                    ? exerciseRepository.findAllByPrimaryMuscleAndPrefix(formattedMuscles, prefix, pageable)
                    : exerciseRepository.findAllByPrimaryMuscle(formattedMuscles, pageable);
        }
        return hasPrefix
                ? exerciseRepository.findByPrefixOrContaining(prefix, pageable)
                : exerciseRepository.findAll(pageable);
    }

    public Exercise findExerciseById(UUID id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id));
    }
}
