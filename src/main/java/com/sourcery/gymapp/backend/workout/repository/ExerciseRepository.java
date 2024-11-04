package com.sourcery.gymapp.backend.workout.repository;

import com.sourcery.gymapp.backend.workout.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    List<Exercise> findAllByIdIn(List<UUID> exerciseIds);

    @Query(value = "SELECT * FROM workout_data.exercise " +
            "WHERE name ILIKE :prefix || '%' " +
            "OR name ILIKE '%' || :prefix || '%' " +
            "ORDER BY (CASE WHEN name ILIKE :prefix || '%' THEN 0 ELSE 1 END), name " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Exercise> findTopByPrefixOrContaining(@Param("prefix") String prefix, @Param("limit") int limit);
}
