package com.sourcery.gymapp.backend.workout.mapper;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.backend.workout.dto.ExerciseSimpleDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutExerciseDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutExerciseSetDto;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Workout;
import com.sourcery.gymapp.backend.workout.model.WorkoutExercise;
import com.sourcery.gymapp.backend.workout.model.WorkoutExerciseSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;

@Component
public class WorkoutExerciseMapper {
    public ResponseWorkoutExerciseDto toDto(WorkoutExercise workoutExercise) {
        var exerciseSimpleDto = new ExerciseSimpleDto(
                workoutExercise.getExercise().getId(),
                workoutExercise.getExercise().getName()
        );
        var responseWorkoutExerciseSetDtos = workoutExercise.getSets()
                .stream()
                .map(this::toResponseWorkoutSetDto)
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
        workoutExercise.setOrderNumber(createWorkoutExerciseDto.orderNumber());
        workoutExercise.setNotes(createWorkoutExerciseDto.notes());
        workoutExercise.setExercise(exercise);
        workoutExercise.setWorkout(workout);
        if (createWorkoutExerciseDto.sets() != null) {
            workoutExercise.setSets(
                    createWorkoutExerciseDto.sets()
                            .stream()
                            .sorted(Comparator.comparingInt(CreateWorkoutExerciseSetDto::setNumber))
                            .map(workoutExerciseSetDto -> toWorkoutSetEntity(workoutExerciseSetDto, workoutExercise))
                            .toList()
            );
        } else {
            workoutExercise.setSets(new ArrayList<>());
        }

        return workoutExercise;
    }

    private ResponseWorkoutExerciseSetDto toResponseWorkoutSetDto(WorkoutExerciseSet workoutExerciseSet) {
        return new ResponseWorkoutExerciseSetDto(
                workoutExerciseSet.getId(),
                workoutExerciseSet.getSetNumber(),
                workoutExerciseSet.getReps(),
                workoutExerciseSet.getWeight(),
                workoutExerciseSet.getRestTime(),
                workoutExerciseSet.getComment()
        );
    }

    private WorkoutExerciseSet toWorkoutSetEntity(
            CreateWorkoutExerciseSetDto createWorkoutExerciseSetDto,
            WorkoutExercise workoutExercise) {

        var workoutExerciseSet = new WorkoutExerciseSet();
        workoutExerciseSet.setWorkoutExercise(workoutExercise);
        workoutExerciseSet.setSetNumber(createWorkoutExerciseSetDto.setNumber());
        workoutExerciseSet.setReps(createWorkoutExerciseSetDto.reps());
        workoutExerciseSet.setWeight(createWorkoutExerciseSetDto.weight());
        workoutExerciseSet.setRestTime(createWorkoutExerciseSetDto.restTime());
        workoutExerciseSet.setComment(createWorkoutExerciseSetDto.comment());

        return workoutExerciseSet;
    }
}
