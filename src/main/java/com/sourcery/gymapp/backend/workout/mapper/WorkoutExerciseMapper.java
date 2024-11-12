package com.sourcery.gymapp.backend.workout.mapper;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ExerciseSimpleDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutExerciseSetDto;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Workout;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;

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
                .sorted(Comparator.comparingInt(ResponseWorkoutExerciseSetDto::setNumber))
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

    public void updateEntity(
            CreateWorkoutExerciseDto workoutExerciseDto,
            WorkoutExercise workoutExercise,
            Exercise exercise
    ) {
        workoutExercise.setOrderNumber(workoutExerciseDto.orderNumber());
        workoutExercise.setNotes(workoutExerciseDto.notes());
        workoutExercise.setExercise(exercise);
    }
}
