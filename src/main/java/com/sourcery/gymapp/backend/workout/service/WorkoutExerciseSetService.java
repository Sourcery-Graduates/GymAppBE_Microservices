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
                    workoutExerciseSetMapper.updateEntity(updateWorkoutExerciseSetDto, workoutExerciseSet);

                    updateWorkoutExerciseSetDtoIds.remove(workoutExerciseSet.getId());
                } else {
                    iterator.remove();
                    workoutExerciseSet.setWorkoutExercise(null);
                }
            }

            for (CreateWorkoutExerciseSetDto workoutExerciseSetDto : createWorkoutExerciseDto.sets()) {
                if (updateWorkoutExerciseSetDtoIds.contains(workoutExerciseSetDto.id())) {
                    var newWorkoutExerciseSet = workoutExerciseSetMapper.toEntity(workoutExerciseSetDto, workoutExercise);
                    workoutExercise.addSet(newWorkoutExerciseSet);
                }
            }
        } else {
            workoutExercise.setSets(new ArrayList<>());
        }
    }
}
