package com.sourcery.gymapp.backend.workout.factory;

import com.sourcery.gymapp.backend.workout.dto.*;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.RoutineExercise;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ExerciseFactory {

    public static Exercise createExercise() {
        Exercise exercise = new Exercise();
        exercise.setId(UUID.randomUUID());
        exercise.setName("Test Exercise");
        exercise.setForce("Test Force");
        exercise.setLevel("Intermediate");
        exercise.setMechanic("Compound");
        exercise.setEquipment("Dumbbell");
        exercise.setPrimaryMuscles(List.of("Chest", "Shoulders"));
        exercise.setSecondaryMuscles(List.of("Triceps"));
        exercise.setDescription(List.of("Step 1: Get into position.", "Step 2: Execute the exercise."));
        exercise.setCategory("Strength");
        exercise.setImages(List.of("image1.jpg", "image2.jpg"));

        return exercise;
    }

    public static ExerciseSimpleDto createExerciseSimpleDto(
            UUID exerciseId,
            String exerciseName) {

        return new ExerciseSimpleDto(
                exerciseId,
                exerciseName
        );
    }

    public static ExerciseSimpleDto createExerciseSimpleDto() {
        return new ExerciseSimpleDto(
                UUID.randomUUID(),
                "Test Exercise"
        );
    }

    public static ExerciseDetailDto createExerciseDetailDto() {
        return new ExerciseDetailDto(
                UUID.randomUUID(),
                "Test Exercise",
                "Push",
                "Intermediate",
                "Compound",
                "Dumbbell",
                List.of("Chest", "Shoulders"),
                List.of("Triceps"),
                List.of("Step 1: Get into position.", "Step 2: Execute the exercise."),
                "Strength",
                List.of("image1.jpg", "image2.jpg")
        );
    }

    public static ExerciseDetailDto createMatchingExerciseDetailDto(Exercise exercise) {
        return new ExerciseDetailDto(
                exercise.getId(),
                exercise.getName(),
                exercise.getForce(),
                exercise.getLevel(),
                exercise.getMechanic(),
                exercise.getEquipment(),
                exercise.getPrimaryMuscles(),
                exercise.getSecondaryMuscles(),
                exercise.getDescription(),
                exercise.getCategory(),
                exercise.getImages()
        );
    }

    public static RoutineExercise createRoutineExercise(Routine routine, Exercise exercise) {
        RoutineExercise routineExercise = new RoutineExercise();
        routineExercise.setId(UUID.randomUUID());
        routineExercise.setRoutine(routine);
        routineExercise.setExercise(exercise);
        routineExercise.setOrderNumber(1);
        routineExercise.setDefaultSets(3);
        routineExercise.setDefaultReps(10);
        routineExercise.setDefaultWeight(BigDecimal.valueOf(50.0));
        routineExercise.setDefaultRestTime(60);
        routineExercise.setNotes("Test notes");

        return routineExercise;
    }

    public static CreateRoutineExerciseDto createRoutineExerciseDto() {
        return new CreateRoutineExerciseDto(
                UUID.randomUUID(),
                1,
                3,
                10,
                BigDecimal.valueOf(50.0),
                60,
                "Test notes"
        );
    }

    public static ResponseRoutineExerciseDto createResponseRoutineExerciseDto(
            ExerciseSimpleDto exerciseSimpleDto) {

        return new ResponseRoutineExerciseDto(
                UUID.randomUUID(),
                exerciseSimpleDto,
                1,
                3,
                10,
                BigDecimal.valueOf(50.0),
                60,
                "Test notes"
        );
    }

    public static ResponseRoutineDetailDto createResponseRoutineDetailExerciseListDto(
            ResponseRoutineDto routineDto,
            List<ResponseRoutineExerciseDto> exercises) {

        ResponseRoutineDto responseRoutineDto = RoutineFactory.createResponseRoutineDto();

        return new ResponseRoutineDetailDto(
            responseRoutineDto,
            exercises
        );
    }
}
