package com.sourcery.gymapp.backend.workout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "exercise", schema = "workout_data")
public class Exercise extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String force;

    private String level;

    private String mechanic;

    private String equipment;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "primary_muscles", columnDefinition = "text[]")
    private List<String> primaryMuscles;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "secondary_muscles", columnDefinition = "text[]")
    private List<String> secondaryMuscles;

    private String description;

    private String category;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "images", columnDefinition = "text[]")
    private List<String> images;
}