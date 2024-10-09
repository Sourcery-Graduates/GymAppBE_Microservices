package com.sourcery.gymapp.backend.workout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "routine_exercise", schema = "workout_data")
public class RoutineExercise extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "routine_id", referencedColumnName = "id", nullable = false)
    private Routine routine;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private Integer orderNumber;

    private Integer defaultSets;

    private Integer defaultReps;

    @Column(precision = 5, scale = 2)
    private BigDecimal defaultWeight;

    private Integer defaultRestTime;

    private String notes;
}