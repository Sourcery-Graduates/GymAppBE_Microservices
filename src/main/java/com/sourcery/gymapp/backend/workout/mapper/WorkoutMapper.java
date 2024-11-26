package com.sourcery.gymapp.backend.workout.mapper;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.Workout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class WorkoutMapper {
    private final WorkoutExerciseMapper exerciseMapper;

    public ResponseWorkoutDto toDto(Workout workout) {
        var responseWorkoutExerciseDtos = workout.getExercises()
                .stream()
                .map(exerciseMapper::toDto)
                .toList();
        UUID basedOnWorkoutId = null;
        UUID routineId = null;

        if (workout.getBasedOnWorkout() != null) {
            basedOnWorkoutId = workout.getBasedOnWorkout().getId();
        }

        if (workout.getRoutine() != null) {
            routineId = workout.getRoutine().getId();
        }

        return new ResponseWorkoutDto(
                workout.getId(),
                workout.getUserId(),
                workout.getName(),
                workout.getDate(),
                workout.getComment(),
                basedOnWorkoutId,
                routineId,
                responseWorkoutExerciseDtos
        );
    }

    public Workout toEntity(
            CreateWorkoutDto dto,
            UUID userId,
            Workout basedOnWorkout,
            Routine routine,
            Map<UUID, Exercise> exerciseMap) {

        var workout = new Workout();
        workout.setUserId(userId);
        workout.setName(dto.name());
        workout.setDate(dto.date());
        workout.setComment(dto.comment());
        workout.setBasedOnWorkout(basedOnWorkout);
        workout.setRoutine(routine);
        mapToWorkoutExerciseList(dto, workout, exerciseMap);

        return workout;
    }

    private void mapToWorkoutExerciseList(CreateWorkoutDto dto, Workout workout, Map<UUID, Exercise> exerciseMap) {
        if (dto.exercises() != null) {
            workout.setExercises(
                    dto.exercises()
                            .stream()
                            .map(createWorkoutExerciseDto -> {
                                var exercise = exerciseMap.get(createWorkoutExerciseDto.exerciseId());

                                return exerciseMapper.toEntity(createWorkoutExerciseDto, exercise, workout);
                            })
                            .toList()
            );
        } else {
            workout.setExercises(new ArrayList<>());
        }
    }
}
