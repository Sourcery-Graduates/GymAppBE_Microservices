package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.exception.ExerciseNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.WorkoutExerciseMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Workout;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;
import com.sourcery.gymapp.backend.workout.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorkoutExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final WorkoutExerciseSetService workoutExerciseSetService;
    private final WorkoutExerciseMapper workoutExerciseMapper;

    public void updateWorkoutExercises(CreateWorkoutDto updateWorkoutDto, Workout workout) {
        if (updateWorkoutDto.exercises() != null && !updateWorkoutDto.exercises().isEmpty()) {
            Set<UUID> updateWorkoutExerciseDtoIds = updateWorkoutDto.exercises().stream()
                    .map(CreateWorkoutExerciseDto::id)
                    .collect(Collectors.toSet());

            updateExistingWorkoutExercises(updateWorkoutDto, workout, updateWorkoutExerciseDtoIds);
            addNewWorkoutExercises(updateWorkoutDto, workout, updateWorkoutExerciseDtoIds);
        } else {
            workout.setExercises(new ArrayList<>());
        }
    }

    private void updateExistingWorkoutExercises(
            CreateWorkoutDto updateWorkoutDto,
            Workout workout,
            Set<UUID> updateWorkoutExerciseDtoIds
    ) {
        for (Iterator<WorkoutExercise> iterator = workout.getExercises().iterator(); iterator.hasNext(); ) {
            WorkoutExercise workoutExercise = iterator.next();

            if (updateWorkoutExerciseDtoIds.contains(workoutExercise.getId())) {
                var updateWorkoutExerciseDto = findWorkoutExerciseDto(updateWorkoutDto, workoutExercise);
                var exercise = updateExercise(updateWorkoutExerciseDto, workoutExercise);

                updateWorkoutExerciseFields(updateWorkoutExerciseDto, workoutExercise, exercise);
                workoutExerciseSetService.updateSets(updateWorkoutExerciseDto, workoutExercise);

                updateWorkoutExerciseDtoIds.remove(workoutExercise.getId());
            } else {
                iterator.remove();
                workoutExercise.setWorkout(null);
            }
        }
    }

    private void addNewWorkoutExercises(
            CreateWorkoutDto updateWorkoutDto,
            Workout workout,
            Set<UUID> updateWorkoutExerciseDtoIds
    ) {
        for (CreateWorkoutExerciseDto workoutExerciseDto : updateWorkoutDto.exercises()) {
            if (updateWorkoutExerciseDtoIds.contains(workoutExerciseDto.id())) {
                var exercise = findExerciseById(workoutExerciseDto.exerciseId());
                var newWorkoutExercise = workoutExerciseMapper.toEntity(workoutExerciseDto, exercise, workout);
                workout.addExercise(newWorkoutExercise);
            }
        }
    }

    private void updateWorkoutExerciseFields(
            CreateWorkoutExerciseDto workoutExerciseDto,
            WorkoutExercise workoutExercise,
            Exercise exercise
    ) {
        workoutExercise.setOrderNumber(workoutExerciseDto.orderNumber());
        workoutExercise.setNotes(workoutExerciseDto.notes());
        workoutExercise.setExercise(exercise);
    }

    private Exercise updateExercise(
            CreateWorkoutExerciseDto updateWorkoutExerciseDto,
            WorkoutExercise workoutExercise
    ) {
        Exercise exercise;

        if (updateWorkoutExerciseDto.exerciseId().equals(workoutExercise.getExercise().getId())) {
            exercise = workoutExercise.getExercise();
        } else {
            exercise = findExerciseById(updateWorkoutExerciseDto.exerciseId());
        }

        return exercise;
    }

    private CreateWorkoutExerciseDto findWorkoutExerciseDto(
            CreateWorkoutDto updateWorkoutDto,
            WorkoutExercise workoutExercise
    ) {
        return updateWorkoutDto.exercises().stream()
                .filter(exerciseDto -> exerciseDto.id() != null && exerciseDto.id().equals(workoutExercise.getId()))
                .findFirst()
                .orElseThrow();
    }

    private Exercise findExerciseById(UUID id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id));
    }
}
