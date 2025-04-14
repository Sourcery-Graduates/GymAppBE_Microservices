package com.sourcery.gymapp.workout.repository;

import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.dto.RoutineWithLikeStatusProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, UUID> {

    @Query("SELECT r AS routine, " +
            "CASE WHEN l.userId = :userId THEN true ELSE false END AS likedByCurrentUser " +
            "FROM Routine r LEFT JOIN RoutineLike l ON r.id = l.routineId AND l.userId = :userId " +
            "WHERE r.id = :routineId")
    RoutineWithLikeStatusProjection findRoutineWithLikeStatusByRoutineId(UUID routineId, UUID userId);

    @Query("SELECT r AS routine, " +
            "CASE WHEN l.userId = :userId THEN true ELSE false END AS likedByCurrentUser " +
            "FROM Routine r LEFT JOIN RoutineLike l ON r.id = l.routineId AND l.userId = :userId " +
            "WHERE r.userId = :userId " +
            "ORDER BY r.likesCount DESC, LOWER(r.name) ASC, r.createdAt DESC")
    List<RoutineWithLikeStatusProjection> findRoutinesWithLikeStatusByUserId(UUID userId);

    @Query("SELECT r AS routine, " +
            "CASE WHEN l.userId = :userId THEN true ELSE false END AS likedByCurrentUser " +
            "FROM Routine r LEFT JOIN RoutineLike l ON r.id = l.routineId AND l.userId = :userId " +
            "ORDER BY r.likesCount DESC, LOWER(r.name) ASC, r.createdAt DESC")
    Page<RoutineWithLikeStatusProjection> findAllWithLikeStatus(UUID userId, Pageable pageable);

    @Query("SELECT r AS routine, " +
            "CASE WHEN l.userId = :userId THEN true ELSE false END AS likedByCurrentUser " +
            "FROM Routine r LEFT JOIN RoutineLike l ON r.id = l.routineId AND l.userId = :userId " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))" +
            "ORDER BY r.likesCount DESC, LOWER(r.name) ASC, r.createdAt DESC")
    Page<RoutineWithLikeStatusProjection> findRoutinesWithLikeStatusByName(UUID userId, String name, Pageable pageable);
}
