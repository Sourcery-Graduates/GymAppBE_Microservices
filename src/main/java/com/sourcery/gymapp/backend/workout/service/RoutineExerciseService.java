package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineListExerciseDto;
import com.sourcery.gymapp.backend.workout.mapper.RoutineExerciseMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.RoutineExercise;
import com.sourcery.gymapp.backend.workout.repository.RoutineExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineExerciseService {
    private final RoutineService routineService;
    private final ExerciseService exerciseService;
    private final RoutineExerciseRepository routineExerciseRepository;
    private final RoutineExerciseMapper routineExerciseMapper;

    @Transactional
    public ResponseRoutineListExerciseDto replaceExercisesInRoutine(
            UUID routineId,
            List<CreateRoutineExerciseDto> createRoutineExerciseDto) {

        Routine routine = routineService.findRoutineById(routineId);

        Set<Exercise> exercises = exerciseService
                .getAllExercisesFromDatabaseById(createRoutineExerciseDto.
                        stream().map(CreateRoutineExerciseDto::exerciseId).toList());
        System.out.println(exercises);

        Map<UUID, Exercise> exerciseMap =
                exercises.stream().collect(Collectors.toMap(Exercise::getId, exercise -> exercise));
        System.out.println(exerciseMap);

        List<RoutineExercise> routineExercises = createRoutineExerciseDto
                .stream().map(
                        exerciseDto -> {
                            Exercise exercise = exerciseMap.get(exerciseDto.exerciseId());

                            return routineExerciseMapper.toEntity(exerciseDto, routine, exercise);
                        }
                )
                .toList();

        routineExerciseRepository.deleteAllByRoutineId(routine.getId());
        routineExerciseRepository.saveAll(routineExercises);

        List<ResponseRoutineExerciseDto> routineExercisesDto = routineExercises
                .stream().map(routineExerciseMapper::toResponseRoutineExerciseDto).toList();

        return new ResponseRoutineListExerciseDto(routineId, routineExercisesDto);
    }

    public ResponseRoutineListExerciseDto getExercisesFromRoutine(UUID routineId) {
        routineService.findRoutineById(routineId);

        List<RoutineExercise> routineExercises = routineExerciseRepository.findAllByRoutineId(routineId);

        List<ResponseRoutineExerciseDto> routineExercisesDto = routineExercises
                .stream().map(routineExerciseMapper::toResponseRoutineExerciseDto).toList();

        return new ResponseRoutineListExerciseDto(routineId, routineExercisesDto);
    }
}
