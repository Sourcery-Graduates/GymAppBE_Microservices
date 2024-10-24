package com.sourcery.gymapp.backend.workout.repository;

import com.sourcery.gymapp.backend.workout.model.RoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, UUID> {

    void deleteAllByRoutineId(UUID routineId);

    List<RoutineExercise> findAllByRoutineId(UUID routineId);
}
