package com.sourcery.gymapp.backend.workout.model;

import jakarta.persistence.*;
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

    private String name;

    private Date date;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "based_on_workout_id", referencedColumnName = "id")
    private Workout basedOnWorkout;

    @ManyToOne
    @JoinColumn(name = "routine_id", referencedColumnName = "id")
    private Routine routine;
}