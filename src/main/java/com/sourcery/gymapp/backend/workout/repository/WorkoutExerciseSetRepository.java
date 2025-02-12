package com.sourcery.gymapp.backend.workout.repository;

import com.sourcery.gymapp.backend.workout.model.WorkoutExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkoutExerciseSetRepository extends JpaRepository<WorkoutExerciseSet, UUID> {
}
