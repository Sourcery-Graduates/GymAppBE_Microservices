package com.sourcery.gymapp.workout.mapper;

import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.workout.dto.ExerciseSimpleDto;
import com.sourcery.gymapp.workout.dto.ResponseWorkoutExerciseDto;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.Workout;
import com.sourcery.gymapp.workout.model.WorkoutExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class WorkoutExerciseMapper {
    private final WorkoutExerciseSetMapper workoutExerciseSetMapper;

    public ResponseWorkoutExerciseDto toDto(WorkoutExercise workoutExercise) {
        var exerciseSimpleDto = new ExerciseSimpleDto(
                workoutExercise.getExercise().getId(),
                workoutExercise.getExercise().getName()
        );
        var responseWorkoutExerciseSetDtos = workoutExercise.getSets()
                .stream()
                .map(workoutExerciseSetMapper::toDto)
                .toList();

        return new ResponseWorkoutExerciseDto(
                workoutExercise.getId(),
                exerciseSimpleDto,
                workoutExercise.getOrderNumber(),
                workoutExercise.getNotes(),
                responseWorkoutExerciseSetDtos
        );
    }

    public WorkoutExercise toEntity(
            CreateWorkoutExerciseDto createWorkoutExerciseDto,
            Exercise exercise,
            Workout workout) {

        var workoutExercise = new WorkoutExercise();
        workoutExercise.setId(createWorkoutExerciseDto.id());
        workoutExercise.setOrderNumber(createWorkoutExerciseDto.orderNumber());
        workoutExercise.setNotes(createWorkoutExerciseDto.notes());
        workoutExercise.setExercise(exercise);
        workoutExercise.setWorkout(workout);
        if (createWorkoutExerciseDto.sets() != null) {
            workoutExercise.setSets(
                    createWorkoutExerciseDto.sets()
                            .stream()
                            .map(workoutExerciseSetDto -> workoutExerciseSetMapper.toEntity(workoutExerciseSetDto, workoutExercise))
                            .toList()
            );
        } else {
            workoutExercise.setSets(new ArrayList<>());
        }

        return workoutExercise;
    }
}
