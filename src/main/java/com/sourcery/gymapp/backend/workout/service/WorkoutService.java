package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.exception.UserNotAuthorizedException;
import com.sourcery.gymapp.backend.workout.exception.WorkoutNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.WorkoutMapper;
import com.sourcery.gymapp.backend.workout.model.*;
import com.sourcery.gymapp.backend.workout.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final RoutineService routineService;
    private final WorkoutCurrentUserService currentUserService;
    private final ExerciseService exerciseService;
    private final WorkoutMapper workoutMapper;

    @Transactional
    public ResponseWorkoutDto createWorkout(CreateWorkoutDto createWorkoutDto) {
        var currentUserId = currentUserService.getCurrentUserId();
        Workout basedOnWorkout = null;
        if (createWorkoutDto.basedOnWorkoutId() != null) {
            basedOnWorkout = findWorkoutById(createWorkoutDto.basedOnWorkoutId());
        }
        Routine routine = null;
        if (createWorkoutDto.routineId() != null) {
            routine = routineService.findRoutineById(createWorkoutDto.routineId());
        }
        Map<UUID, Exercise> exerciseMap = new HashMap<>();
        if (createWorkoutDto.exercises() != null) {
            exerciseMap = exerciseService.getExerciseMapByIds(
                    createWorkoutDto.exercises()
                            .stream()
                            .map(CreateWorkoutExerciseDto::exerciseId)
                            .toList()
            );
        }

        var workout = workoutMapper.toEntity(createWorkoutDto, currentUserId, basedOnWorkout, routine, exerciseMap);
        workoutRepository.save(workout);

        return workoutMapper.toDto(workout);
    }

    @Transactional
    public ResponseWorkoutDto updateWorkout(CreateWorkoutDto updateWorkoutDto, UUID workoutId) {
        var workout = findWorkoutById(workoutId);
        var currentUserId = currentUserService.getCurrentUserId();

        checkIsUserAuthorized(currentUserId, workout.getUserId());

        Map<UUID, Exercise> exerciseMap = new HashMap<>();
        if (updateWorkoutDto.exercises() != null) {
            exerciseMap = exerciseService.getExerciseMapByIds(
                    updateWorkoutDto.exercises()
                            .stream()
                            .map(CreateWorkoutExerciseDto::exerciseId)
                            .toList()
            );
        }

        workoutMapper.updateEntity(updateWorkoutDto, workout, exerciseMap);
        workoutRepository.save(workout);

        return workoutMapper.toDto(workout);
    }

    public ResponseWorkoutDto getWorkoutById(UUID workoutId) {
        Workout workout = findWorkoutById(workoutId);

        return workoutMapper.toDto(workout);
    }

    public List<ResponseWorkoutDto> getWorkoutsByUserId() {
        var currentUserId = currentUserService.getCurrentUserId();

        List<Workout> workouts = workoutRepository.findByUserId(currentUserId);

        return workouts.stream()
                .map(workoutMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteWorkout(UUID workoutId) {
        var currentUserId = currentUserService.getCurrentUserId();
        var workout = findWorkoutById(workoutId);

        checkIsUserAuthorized(currentUserId, workout.getUserId());

        workoutRepository.delete(workout);
    }

    public Workout findWorkoutById(UUID id) {

        return workoutRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException(id));
    }

    private void checkIsUserAuthorized(UUID currentUserId, UUID workoutUserId) {

        if (!workoutUserId.equals(currentUserId)) {
            throw new UserNotAuthorizedException();
        }
    }
}
