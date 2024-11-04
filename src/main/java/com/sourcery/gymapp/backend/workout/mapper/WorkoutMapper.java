package com.sourcery.gymapp.backend.workout.mapper;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.Workout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
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

        return new ResponseWorkoutDto(
                workout.getId(),
                workout.getName(),
                workout.getDate(),
                workout.getComment(),
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
        if (dto.exercises() != null) {
            workout.setExercises(
                    dto.exercises()
                            .stream()
                            .sorted(Comparator.comparingInt(CreateWorkoutExerciseDto::orderNumber))
                            .map(createWorkoutExerciseDto -> {
                                var exercise = exerciseMap.get(createWorkoutExerciseDto.exerciseId());

                                return exerciseMapper.toEntity(createWorkoutExerciseDto, exercise, workout);
                            })
                            .toList()
            );
        } else {
            workout.setExercises(new ArrayList<>());
        }

        return workout;
    }
}
