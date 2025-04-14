package com.sourcery.gymapp.workout.repository;

import com.sourcery.gymapp.workout.model.WorkoutExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkoutExerciseSetRepository extends JpaRepository<WorkoutExerciseSet, UUID> {
}
