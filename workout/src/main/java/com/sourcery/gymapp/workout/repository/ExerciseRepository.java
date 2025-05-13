package com.sourcery.gymapp.workout.repository;

import com.sourcery.gymapp.workout.model.Exercise;
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

    @Query(value = "SELECT * FROM exercise " +
            "WHERE name ILIKE :prefix || '%' " +
            "OR name ILIKE '%' || :prefix || '%' " +
            "ORDER BY (CASE WHEN name ILIKE :prefix || '%' THEN 0 ELSE 1 END), name",
            countQuery = "SELECT COUNT(*) FROM exercise " +
                    "WHERE name ILIKE :prefix || '%' OR name ILIKE '%' || :prefix || '%'",
            nativeQuery = true)
    Page<Exercise> findByPrefixOrContaining(@Param("prefix") String prefix, Pageable pageable);


    @Query(value = "SELECT * FROM exercise " +
            "WHERE primary_muscles @> CAST(:primaryMuscle AS text[])",
            countQuery = "SELECT COUNT(*) FROM exercise " +
                    "WHERE primary_muscles @> CAST(:primaryMuscle AS text[])",
            nativeQuery = true)
    Page<Exercise> findAllByPrimaryMuscle(@Param("primaryMuscle") String primaryMuscle, Pageable pageable);

    @Query(value = "SELECT * FROM exercise " +
            "WHERE primary_muscles @> CAST(:primaryMuscle AS text[]) " +
            "AND (name ILIKE :prefix || '%' " +
            "OR name ILIKE '%' || :prefix || '%')",
            countQuery = "SELECT COUNT(*) FROM exercise " +
                    "WHERE primary_muscles @> CAST(:primaryMuscle AS text[]) " +
                    "AND (name ILIKE :prefix || '%' OR name ILIKE '%' || :prefix || '%')",
            nativeQuery = true)
    Page<Exercise> findAllByPrimaryMuscleAndPrefix(@Param("primaryMuscle") String primaryMuscle,
                                                   @Param("prefix") String prefix,
                                                   Pageable pageable);
}
