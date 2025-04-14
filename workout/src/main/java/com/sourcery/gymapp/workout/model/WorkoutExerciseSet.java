package com.sourcery.gymapp.workout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
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

  @Size(max = 255)
  private String comment;
}