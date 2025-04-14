package com.sourcery.gymapp.workout.repository;

import com.sourcery.gymapp.workout.model.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {
}
