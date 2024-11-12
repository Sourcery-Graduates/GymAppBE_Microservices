package com.sourcery.gymapp.backend.workout.repository;

import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
    List<Workout> findByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("UPDATE Workout w SET w.basedOnWorkout = null WHERE w.basedOnWorkout = :workout")
    void setBasedOnWorkoutToNull(@Param("workout") Workout workout);

    @Modifying
    @Transactional
    @Query("UPDATE Workout w SET w.routine = null WHERE w.routine = :routine")
    void setRoutineToNull(@Param("routine") Routine routine);
}
