package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.backend.workout.mapper.WorkoutExerciseSetMapper;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;
import com.sourcery.gymapp.backend.workout.model.WorkoutExerciseSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorkoutExerciseSetService {
    private final WorkoutExerciseSetMapper workoutExerciseSetMapper;

    public void updateSets(
            CreateWorkoutExerciseDto createWorkoutExerciseDto,
            WorkoutExercise workoutExercise) {

        if (createWorkoutExerciseDto.sets() != null && !createWorkoutExerciseDto.sets().isEmpty()) {
            Set<UUID> updateWorkoutExerciseSetDtoIds = createWorkoutExerciseDto.sets().stream()
                    .map(CreateWorkoutExerciseSetDto::id)
                    .collect(Collectors.toSet());

            updateExistingWorkoutExerciseSets(createWorkoutExerciseDto, workoutExercise, updateWorkoutExerciseSetDtoIds);
            addNewWorkoutExerciseSets(createWorkoutExerciseDto, workoutExercise, updateWorkoutExerciseSetDtoIds);
        } else {
            workoutExercise.setSets(new ArrayList<>());
        }
    }

    private void updateExistingWorkoutExerciseSets(
            CreateWorkoutExerciseDto createWorkoutExerciseDto,
            WorkoutExercise workoutExercise,
            Set<UUID> updateWorkoutExerciseSetDtoIds
    ) {
        for (Iterator<WorkoutExerciseSet> iterator = workoutExercise.getSets().iterator(); iterator.hasNext(); ) {
            WorkoutExerciseSet workoutExerciseSet = iterator.next();

            if (updateWorkoutExerciseSetDtoIds.contains(workoutExerciseSet.getId())) {
                var updateWorkoutExerciseSetDto = findWorkoutExerciseSetDto(createWorkoutExerciseDto, workoutExerciseSet);
                updateWorkoutExerciseSetFields(updateWorkoutExerciseSetDto, workoutExerciseSet);

                updateWorkoutExerciseSetDtoIds.remove(workoutExerciseSet.getId());
            } else {
                iterator.remove();
                workoutExerciseSet.setWorkoutExercise(null);
            }
        }
    }

    private void addNewWorkoutExerciseSets(
            CreateWorkoutExerciseDto createWorkoutExerciseDto,
            WorkoutExercise workoutExercise,
            Set<UUID> updateWorkoutExerciseSetDtoIds) {
        for (CreateWorkoutExerciseSetDto workoutExerciseSetDto : createWorkoutExerciseDto.sets()) {
            if (updateWorkoutExerciseSetDtoIds.contains(workoutExerciseSetDto.id())) {
                var newWorkoutExerciseSet = workoutExerciseSetMapper.toEntity(workoutExerciseSetDto, workoutExercise);
                workoutExercise.addSet(newWorkoutExerciseSet);
            }
        }
    }

    private void updateWorkoutExerciseSetFields(
            CreateWorkoutExerciseSetDto workoutExerciseSetDto,
            WorkoutExerciseSet workoutExerciseSet
    ) {
        workoutExerciseSet.setSetNumber(workoutExerciseSetDto.setNumber());
        workoutExerciseSet.setReps(workoutExerciseSetDto.reps());
        workoutExerciseSet.setWeight(workoutExerciseSetDto.weight());
        workoutExerciseSet.setRestTime(workoutExerciseSetDto.restTime());
        workoutExerciseSet.setComment(workoutExerciseSetDto.comment());
    }

    private CreateWorkoutExerciseSetDto findWorkoutExerciseSetDto(
            CreateWorkoutExerciseDto createWorkoutExerciseDto,
            WorkoutExerciseSet workoutExerciseSet
    ) {
        return createWorkoutExerciseDto.sets().stream()
                .filter(setDto -> setDto.id() != null && setDto.id().equals(workoutExerciseSet.getId()))
                .findFirst()
                .orElseThrow();
    }
}
