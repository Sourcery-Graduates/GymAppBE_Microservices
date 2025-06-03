package com.sourcery.gymapp.workout.service;

import com.sourcery.gymapp.workout.dto.CreateRoutineExerciseDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineExerciseDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineDetailDto;
import com.sourcery.gymapp.workout.mapper.RoutineExerciseMapper;
import com.sourcery.gymapp.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.model.RoutineExercise;
import com.sourcery.gymapp.workout.repository.RoutineExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoutineExerciseService {
    private final RoutineService routineService;
    private final ExerciseService exerciseService;
    private final RoutineExerciseRepository routineExerciseRepository;
    private final RoutineExerciseMapper routineExerciseMapper;
    private final RoutineMapper routineMapper;

    @Transactional
    public ResponseRoutineDetailDto updateExercisesInARoutine(
            UUID routineId,
            List<CreateRoutineExerciseDto> createRoutineExerciseDto) {

        Routine routine = routineService.findRoutineById(routineId);

        List<RoutineExercise> existingExercises = routineExerciseRepository.findAllByRoutineId(routineId);
        routineExerciseRepository.deleteAll(existingExercises);

        Map<UUID, Exercise> exerciseMap = exerciseService
                .getExerciseMapByIds(createRoutineExerciseDto.
                        stream().map(CreateRoutineExerciseDto::exerciseId).toList());

        List<RoutineExercise> routineExercises = createRoutineExerciseDto.stream()
                .sorted(Comparator.comparingInt(CreateRoutineExerciseDto::orderNumber))
                .map(exerciseDto -> {
                            Exercise exercise = exerciseMap.get(exerciseDto.exerciseId());

                            return routineExerciseMapper.toEntity(exerciseDto, routine, exercise);
                        }
                )
                .toList();

        routineExerciseRepository.saveAll(routineExercises);

        return mapToResponseDto(routine, routineExercises);
    }

    public ResponseRoutineDetailDto getRoutineDetails(UUID routineId) {
        Routine routine = routineService.findRoutineById(routineId);

        List<RoutineExercise> routineExercises = routineExerciseRepository.findAllByRoutineId(routineId)
                .stream()
                .sorted(Comparator.comparing(RoutineExercise::getOrderNumber))
                .toList();

        return mapToResponseDto(routine, routineExercises);
    }

    private ResponseRoutineDetailDto mapToResponseDto(Routine routine, List<RoutineExercise> routineExercises) {
        List<ResponseRoutineExerciseDto> routineExercisesDto = routineExercises
                .stream()
                .map(routineExerciseMapper::toResponseRoutineExerciseDto)
                .toList();

        ResponseRoutineDto routineDto = routineMapper.toDto(routine);

        return new ResponseRoutineDetailDto(routineDto, routineExercisesDto);
    }
}
