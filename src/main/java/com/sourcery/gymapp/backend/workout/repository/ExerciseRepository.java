package com.sourcery.gymapp.backend.workout.repository;

import com.sourcery.gymapp.backend.workout.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    List<Exercise> findAllByIdIn(List<UUID> exerciseIds);

    @Query(value = "SELECT * FROM workout_data.exercise " +
            "WHERE name ILIKE :prefix || '%' " +
            "OR name ILIKE '%' || :prefix || '%' " +
            "ORDER BY (CASE WHEN name ILIKE :prefix || '%' THEN 0 ELSE 1 END), name",
            countQuery = "SELECT COUNT(*) FROM workout_data.exercise " +
                    "WHERE name ILIKE :prefix || '%' OR name ILIKE '%' || :prefix || '%'",
            nativeQuery = true)
    Page<Exercise> findByPrefixOrContaining(@Param("prefix") String prefix, Pageable pageable);

}
