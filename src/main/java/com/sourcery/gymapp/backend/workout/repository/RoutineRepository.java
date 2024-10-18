package com.sourcery.gymapp.backend.workout.repository;

import com.sourcery.gymapp.backend.workout.model.Routine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, UUID> {
    List<Routine> findByUserId(UUID userId);

    @Query("SELECT r FROM Routine r WHERE r.name LIKE %?1%")
    List<Routine> findByNameContaining(String name, Pageable pageable);
}
