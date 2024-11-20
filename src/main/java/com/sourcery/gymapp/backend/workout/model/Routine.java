package com.sourcery.gymapp.backend.workout.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "routine", schema = "workout_data")
public class Routine extends BaseEntity {

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    @Size(max = 255)
    private String name;

    @Size(max = 3000)
    private String description;

    @Column(nullable = false)
    private long likesCount;
}
