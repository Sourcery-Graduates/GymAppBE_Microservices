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
                workoutExerciseSetService.updateSets(updateWorkoutExerciseDto, workoutExercise);

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
    }
}
