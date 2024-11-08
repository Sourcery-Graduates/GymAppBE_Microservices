package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.exception.ExerciseNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.UserNotAuthorizedException;
import com.sourcery.gymapp.backend.workout.exception.WorkoutNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.WorkoutExerciseMapper;
import com.sourcery.gymapp.backend.workout.mapper.WorkoutMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.Workout;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;
import com.sourcery.gymapp.backend.workout.model.WorkoutExerciseSet;
import com.sourcery.gymapp.backend.workout.repository.ExerciseRepository;
import com.sourcery.gymapp.backend.workout.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineService routineService;
    private final WorkoutCurrentUserService currentUserService;
    private final ExerciseService exerciseService;
    private final WorkoutMapper workoutMapper;
    private final WorkoutExerciseMapper workoutExerciseMapper;

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

        workoutMapper.updateEntity(updateWorkoutDto, workout);

        if (updateWorkoutDto.exercises() != null) {
            Set<UUID> updateWorkoutExerciseDtoIds = updateWorkoutDto.exercises().stream()
                    .map(CreateWorkoutExerciseDto::id)
                    .collect(Collectors.toSet());

            for (Iterator<WorkoutExercise> iterator = workout.getExercises().iterator(); iterator.hasNext(); ) {
                WorkoutExercise workoutExercise = iterator.next();

                if (updateWorkoutExerciseDtoIds.contains(workoutExercise.getId())) {
                    var updateWorkoutExerciseDto = updateWorkoutDto.exercises().stream()
                            .filter(exerciseDto -> exerciseDto.id() != null && exerciseDto.id().equals(workoutExercise.getId()))
                            .findFirst()
                            .orElseThrow();
                    Exercise exercise;

                    if (updateWorkoutExerciseDto.exerciseId().equals(workoutExercise.getExercise().getId())) {
                        exercise = workoutExercise.getExercise();
                    } else {
                        exercise = exerciseRepository.findById(updateWorkoutExerciseDto.exerciseId())
                                .orElseThrow(() -> new ExerciseNotFoundException(updateWorkoutExerciseDto.exerciseId()));
                    }

                    workoutExerciseMapper.updateEntity(updateWorkoutExerciseDto, workoutExercise, exercise);
                    updateSets(updateWorkoutExerciseDto, workoutExercise);

                    updateWorkoutExerciseDtoIds.remove(workoutExercise.getId());
                } else {
                    iterator.remove();
                    workoutExercise.setWorkout(null);
                }
            }

            for (CreateWorkoutExerciseDto workoutExerciseDto : updateWorkoutDto.exercises()) {
                if (updateWorkoutExerciseDtoIds.contains(workoutExerciseDto.id())) {
                    var exercise = exerciseRepository.findById(workoutExerciseDto.exerciseId())
                            .orElseThrow(() -> new ExerciseNotFoundException(workoutExerciseDto.exerciseId()));
                    var newWorkoutExercise = workoutExerciseMapper.toEntity(workoutExerciseDto, exercise, workout);
                    workout.addExercise(newWorkoutExercise);
                }
            }
        } else {
            workout.setExercises(new ArrayList<>());
        }

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

    private void updateSets(
            CreateWorkoutExerciseDto createWorkoutExerciseDto,
            WorkoutExercise workoutExercise) {

        if (createWorkoutExerciseDto.sets() != null) {
            Set<UUID> updateWorkoutExerciseSetDtoIds = createWorkoutExerciseDto.sets().stream()
                    .map(CreateWorkoutExerciseSetDto::id)
                    .collect(Collectors.toSet());

            for (Iterator<WorkoutExerciseSet> iterator = workoutExercise.getSets().iterator(); iterator.hasNext(); ) {
                WorkoutExerciseSet workoutExerciseSet = iterator.next();

                if (updateWorkoutExerciseSetDtoIds.contains(workoutExerciseSet.getId())) {
                    var updateWorkoutExerciseSetDto = createWorkoutExerciseDto.sets().stream()
                            .filter(setDto -> setDto.id() != null && setDto.id().equals(workoutExerciseSet.getId()))
                            .findFirst()
                            .orElseThrow();
                    workoutExerciseMapper.updateWorkoutExerciseSet(updateWorkoutExerciseSetDto, workoutExerciseSet);

                    updateWorkoutExerciseSetDtoIds.remove(workoutExerciseSet.getId());
                } else {
                    iterator.remove();
                    workoutExerciseSet.setWorkoutExercise(null);
                }
            }

            for (CreateWorkoutExerciseSetDto workoutExerciseSetDto : createWorkoutExerciseDto.sets()) {
                if (updateWorkoutExerciseSetDtoIds.contains(workoutExerciseSetDto.id())) {
                    var newWorkoutExerciseSet = workoutExerciseMapper.toWorkoutSetEntity(workoutExerciseSetDto, workoutExercise);
                    workoutExercise.addSet(newWorkoutExerciseSet);
                }
            }
        } else {
            workoutExercise.setSets(new ArrayList<>());
        }
    }
}
