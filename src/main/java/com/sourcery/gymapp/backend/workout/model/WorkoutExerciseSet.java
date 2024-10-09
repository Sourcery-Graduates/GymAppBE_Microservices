package com.sourcery.gymapp.backend.workout.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "workout_exercise_set", schema = "workout_data")
public class WorkoutExerciseSet extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "workout_exercise_id", referencedColumnName = "id", nullable = false)
  private WorkoutExercise workoutExercise;

  @Column(nullable = false)
  private Integer setNumber;

  private Integer reps;

  @Column(precision = 5, scale = 2)
  private BigDecimal weight;

  private Integer restTime;

  @Min(1)
  @Max(10)
  private Integer rpe;

  private String comment;
}