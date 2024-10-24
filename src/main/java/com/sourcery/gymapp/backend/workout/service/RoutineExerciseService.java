package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.CreateRoutineGridExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineGridExerciseDto;
import com.sourcery.gymapp.backend.workout.mapper.RoutineExerciseMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.RoutineExercise;
import com.sourcery.gymapp.backend.workout.repository.ExerciseRepository;
import com.sourcery.gymapp.backend.workout.repository.RoutineExerciseRepository;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineExerciseService {
    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineExerciseRepository routineExerciseRepository;
    private final RoutineExerciseMapper routineExerciseMapper;

    @Transactional
    public CreateRoutineGridExerciseDto updateExercisesInRoutine(
            UUID routineId,
            List<CreateRoutineExerciseDto> createRoutineExerciseDto) {

        Routine routine = routineRepository.findById(routineId).orElseThrow(
                () -> new IllegalArgumentException("Routine not found")
        );

        List<RoutineExercise> routineExercises = createRoutineExerciseDto
                .stream().map(
                        exercise -> routineExerciseMapper
                                .toEntity(exercise, routine, getExerciseFromDatabaseById(exercise.exerciseId())))
                .toList();

        routineExerciseRepository.deleteAllByRoutineId(routine.getId());
        routineExerciseRepository.saveAll(routineExercises);

        List<CreateRoutineExerciseDto> routineExercisesDto = routineExercises.stream().map(routineExerciseMapper::toCreateRoutineExerciseDto).toList();

        return new CreateRoutineGridExerciseDto(routineId, routineExercisesDto);
    }

    public ResponseRoutineGridExerciseDto getExercisesFromRoutine(UUID routineId) {
        routineRepository.findById(routineId).orElseThrow(
                () -> new IllegalArgumentException("Routine not found")
        );

        List<RoutineExercise> routineExercises = routineExerciseRepository.findAllByRoutineId(routineId);

        List<ResponseRoutineExerciseDto> routineExercisesDto = routineExercises.stream().map(routineExerciseMapper::toResponseRoutineExerciseDto).toList();

        return new ResponseRoutineGridExerciseDto(routineId, routineExercisesDto);
    }

    private Exercise getExerciseFromDatabaseById(UUID exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));
    }
}
