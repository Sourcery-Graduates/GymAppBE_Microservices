package com.sourcery.gymapp.backend.workout.repository;

import com.sourcery.gymapp.backend.workout.model.Workout;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    List<Workout> findByUserId(UUID userId, Sort sort);

    List<Workout> findByUserIdAndDateBetween(UUID userId, ZonedDateTime startDate,
                                             ZonedDateTime endDate, Sort sort);
}
