package com.sourcery.gymapp.workout.repository;

import com.sourcery.gymapp.workout.dto.MuscleSetDto;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.model.Workout;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    List<Workout> findByUserId(UUID userId, Sort sort);

    List<Workout> findByUserIdAndDateBetween(UUID userId, ZonedDateTime startDate,
                                             ZonedDateTime endDate, Sort sort);

    @Query(
            "SELECT COUNT(w) " +
            "FROM Workout w " +
            "WHERE (w.userId = :currentUserId) " +
            "AND (w.date BETWEEN :startOfTheMonth AND :endOfTheMonth)"
    )
    int countWorkoutsByUserIdAndDateBetween(UUID currentUserId,
                                                          ZonedDateTime startOfTheMonth,
                                                          ZonedDateTime endOfTheMonth);

    @Query(
            "SELECT SUM(wes.setNumber * wes.reps * wes.weight) as total_weight, w.userId " +
            "FROM WorkoutExerciseSet wes " +
            "LEFT JOIN WorkoutExercise we " +
            "ON wes.workoutExercise.id = we.id " +
            "LEFT JOIN Workout w " +
            "ON we.workout.id = w.id " +
            "WHERE (w.userId = :currentUserId) " +
            "AND (w.date BETWEEN :startOfTheMonth AND :endOfTheMonth)" +
            "GROUP BY w.userId"
    )
    Optional<Integer> getTotalWeightByUserIdAndDateBetween(
            UUID currentUserId, ZonedDateTime startOfTheMonth, ZonedDateTime endOfTheMonth);

    @Query(
            "SELECT new com.sourcery.gymapp.workout.dto.MuscleSetDto(e.primaryMuscles, SUM(wes.setNumber)) " +
            "FROM Workout as w " +
            "RIGHT JOIN WorkoutExercise as we " +
            "ON w.id = we.workout.id " +
            "RIGHT JOIN  WorkoutExerciseSet as wes " +
            "ON we.id = wes.workoutExercise.id " +
            "RIGHT JOIN Exercise e " +
            "ON we.exercise.id = e.id " +
            "WHERE (w.userId = :currentUserId) " +
            "AND (w.date BETWEEN :startOfTheWeek AND :endOfTheWeek) " +
            "GROUP BY e.primaryMuscles"
    )
    List<MuscleSetDto> getTotalMuscleSetsByUserIdAndDateBetween(
            UUID currentUserId, ZonedDateTime startOfTheWeek, ZonedDateTime endOfTheWeek);

    @Query(
            "SELECT r as routine " +
            "FROM Routine r " +
            "LEFT JOIN Workout w " +
            "ON r.id = w.routine.id " +
            "WHERE (w.userId = :currentUserId) " +
            "AND (w.date BETWEEN :startOfTheMonth AND :endOfTheMonth)" +
            "GROUP BY r " +
            "ORDER BY COUNT(w.id) DESC")
    List<Routine> getMostUsedRoutinesByUserIdAndDateBetween(
            UUID currentUserId, ZonedDateTime startOfTheMonth, ZonedDateTime endOfTheMonth);
}
