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
import java.util.Map;
import java.util.UUID;

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
                            .map(workoutExerciseSetDto -> toWorkoutSetEntity(workoutExerciseSetDto, workoutExercise))
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

    public void updateWorkoutExerciseSet(
            CreateWorkoutExerciseSetDto workoutExerciseSetDto,
            WorkoutExerciseSet workoutExerciseSet
    ) {
        workoutExerciseSet.setSetNumber(workoutExerciseSetDto.setNumber());
        workoutExerciseSet.setReps(workoutExerciseSetDto.reps());
        workoutExerciseSet.setWeight(workoutExerciseSetDto.weight());
        workoutExerciseSet.setRestTime(workoutExerciseSetDto.restTime());
        workoutExerciseSet.setComment(workoutExerciseSetDto.comment());
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

    public WorkoutExerciseSet toWorkoutSetEntity(
            CreateWorkoutExerciseSetDto createWorkoutExerciseSetDto,
            WorkoutExercise workoutExercise) {

        var workoutExerciseSet = new WorkoutExerciseSet();
        workoutExerciseSet.setId(createWorkoutExerciseSetDto.id());
        workoutExerciseSet.setWorkoutExercise(workoutExercise);
        workoutExerciseSet.setSetNumber(createWorkoutExerciseSetDto.setNumber());
        workoutExerciseSet.setReps(createWorkoutExerciseSetDto.reps());
        workoutExerciseSet.setWeight(createWorkoutExerciseSetDto.weight());
        workoutExerciseSet.setRestTime(createWorkoutExerciseSetDto.restTime());
        workoutExerciseSet.setComment(createWorkoutExerciseSetDto.comment());

        return workoutExerciseSet;
    }
}
