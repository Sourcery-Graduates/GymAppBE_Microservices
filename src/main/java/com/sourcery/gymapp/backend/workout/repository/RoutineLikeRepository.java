package com.sourcery.gymapp.backend.workout.repository;

import com.sourcery.gymapp.backend.workout.model.routine_like.RoutineLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoutineLikeRepository extends JpaRepository<RoutineLike, UUID> {

    @Query(value = "INSERT INTO workout_data.routine_like (routine_id, user_id, created_at) " +
            "VALUES (:routineId, :userId, DEFAULT) " +
            "ON CONFLICT DO NOTHING " +
            "RETURNING routine_id", nativeQuery = true)
    Optional<UUID> insertIfNotExists(@Param("routineId") UUID routineId, @Param("userId") UUID userId);

    int deleteByRoutineIdAndUserId(UUID routineId, UUID userId);
}
