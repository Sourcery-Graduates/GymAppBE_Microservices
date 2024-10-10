package com.sourcery.gymapp.backend.workout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workout_exercise", schema = "workout_data")
public class WorkoutExercise extends BaseEntity {

    @Column(nullable = false)
    private Integer orderNumber;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id", nullable = false)
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "id", nullable = false)
    private Workout workout;
}