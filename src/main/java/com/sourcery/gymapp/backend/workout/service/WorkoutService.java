package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.validation.ValidateOrderNumbersInCreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.exception.UserNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.WorkoutNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.WorkoutMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.Workout;
import com.sourcery.gymapp.backend.workout.repository.WorkoutRepository;
import com.sourcery.gymapp.backend.workout.util.AuthorizationUtil;
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
    private final WorkoutExerciseService workoutExerciseService;

    @Transactional
    @ValidateOrderNumbersInCreateWorkoutDto
    public ResponseWorkoutDto createWorkout(CreateWorkoutDto createWorkoutDto) {
        var currentUserId = currentUserService.getCurrentUserId();
        if (currentUserId == null) {
            throw new UserNotFoundException();
        }

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
        workout = workoutRepository.save(workout);

        return workoutMapper.toDto(workout);
    }

    @Transactional
    @ValidateOrderNumbersInCreateWorkoutDto
    public ResponseWorkoutDto updateWorkout(CreateWorkoutDto updateWorkoutDto, UUID workoutId) {
        var workout = findWorkoutById(workoutId);
        var currentUserId = currentUserService.getCurrentUserId();

        AuthorizationUtil.checkIsUserAuthorized(currentUserId, workout.getUserId());

        updateWorkoutFields(updateWorkoutDto, workout);
        workoutExerciseService.updateWorkoutExercises(updateWorkoutDto, workout);

        workout = workoutRepository.save(workout);

        return workoutMapper.toDto(workout);
    }

    public ResponseWorkoutDto getWorkoutById(UUID workoutId) {
        Workout workout = findWorkoutById(workoutId);

        return workoutMapper.toDto(workout);
    }

    public List<ResponseWorkoutDto> getWorkoutsByUserId() {
        var currentUserId = currentUserService.getCurrentUserId();
        if (currentUserId == null) {
            throw new UserNotFoundException();
        }

        List<Workout> workouts = workoutRepository.findByUserId(currentUserId);

        return workouts.stream()
                .map(workoutMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteWorkout(UUID workoutId) {
        var workout = findWorkoutById(workoutId);
        var currentUserId = currentUserService.getCurrentUserId();

        AuthorizationUtil.checkIsUserAuthorized(currentUserId, workout.getUserId());

        workoutRepository.delete(workout);
    }

    public Workout findWorkoutById(UUID id) {

        return workoutRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException(id));
    }

    private void updateWorkoutFields(
            CreateWorkoutDto dto,
            Workout workout) {

        workout.setName(dto.name());
        workout.setDate(dto.date());
        workout.setComment(dto.comment());
    }
}
