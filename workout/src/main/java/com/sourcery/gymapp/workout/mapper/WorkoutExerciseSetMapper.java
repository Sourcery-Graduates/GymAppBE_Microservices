package com.sourcery.gymapp.workout.mapper;

import com.sourcery.gymapp.workout.dto.CreateWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.dto.ResponseWorkoutExerciseSetDto;
import com.sourcery.gymapp.workout.model.WorkoutExercise;
import com.sourcery.gymapp.workout.model.WorkoutExerciseSet;
import org.springframework.stereotype.Component;

@Component
public class WorkoutExerciseSetMapper {

    public ResponseWorkoutExerciseSetDto toDto(WorkoutExerciseSet workoutExerciseSet) {
        return new ResponseWorkoutExerciseSetDto(
                workoutExerciseSet.getId(),
                workoutExerciseSet.getSetNumber(),
                workoutExerciseSet.getReps(),
                workoutExerciseSet.getWeight(),
                workoutExerciseSet.getRestTime(),
                workoutExerciseSet.getComment()
        );
    }

    public WorkoutExerciseSet toEntity(
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
