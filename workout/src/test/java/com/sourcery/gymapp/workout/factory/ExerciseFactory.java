package com.sourcery.gymapp.workout.factory;

import com.sourcery.gymapp.workout.dto.CreateRoutineExerciseDto;
import com.sourcery.gymapp.workout.dto.ExerciseDetailDto;
import com.sourcery.gymapp.workout.dto.ExerciseSimpleDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineDetailDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineExerciseDto;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.model.RoutineExercise;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ExerciseFactory {

    public static Exercise createExercise(UUID uuid, String name, List<String> primaryMuscles, String force, String level, String mechanic, String equipment, List<String> secondaryMuscles, List<String> description, String category, List<String> images) {
        Exercise exercise = new Exercise();
        exercise.setId(uuid);
        exercise.setName(name);
        exercise.setForce(force);
        exercise.setLevel(level);
        exercise.setMechanic(mechanic);
        exercise.setEquipment(equipment);
        exercise.setPrimaryMuscles(primaryMuscles);
        exercise.setSecondaryMuscles(secondaryMuscles);
        exercise.setDescription(description);
        exercise.setCategory(category);
        exercise.setImages(images);
        return exercise;
    }

    public static Exercise createExercise() {
        return createExercise(
                UUID.randomUUID(),
                "Test Exercise",
                List.of("chest"),
                "test force",
                "intermediate",
                "compound",
                "dumbbell",
                List.of("chest, triceps"),
                List.of("Step 1: Get into position.", "Step 2: Execute the exercise."),
                "strength",
                List.of("image1.jpg", "image2.jpg")
        );
    }

    public static Exercise createExercise(UUID uuid, String name) {
        return createExercise(
                uuid,
                name,
                List.of("chest"),
                "test force",
                "intermediate",
                "compound",
                "dumbbell",
                List.of("chest, triceps"),
                List.of("Step 1: Get into position.", "Step 2: Execute the exercise."),
                "strength",
                List.of("image1.jpg", "image2.jpg")
        );
    }

    public static Exercise createExercise(UUID uuid, String name, List<String> primaryMuscles) {
        return createExercise(
                uuid,
                name,
                primaryMuscles,
                "test force",
                "intermediate",
                "compound",
                "dumbbell",
                List.of("chest, triceps"),
                List.of("Step 1: Get into position.", "Step 2: Execute the exercise."),
                "strength",
                List.of("image1.jpg", "image2.jpg")
        );
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
