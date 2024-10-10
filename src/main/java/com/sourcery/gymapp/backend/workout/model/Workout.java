package com.sourcery.gymapp.backend.workout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "workout", schema = "workout_data")
public class Workout extends BaseEntity {

    @Column(nullable = false)
    private UUID userId;

    @Size(max = 255)
    private String name;

    private Date date;

    @Size(max = 255)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "based_on_workout_id", referencedColumnName = "id")
    private Workout basedOnWorkout;

    @ManyToOne
    @JoinColumn(name = "routine_id", referencedColumnName = "id")
    private Routine routine;
}