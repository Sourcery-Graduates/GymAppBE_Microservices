package com.sourcery.gymapp.backend.workout.repository;

import com.sourcery.gymapp.backend.workout.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    Set<Exercise> findAllByIdIn(List<UUID> exerciseIds);
}
