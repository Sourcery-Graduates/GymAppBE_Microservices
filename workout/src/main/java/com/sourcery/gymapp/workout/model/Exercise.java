package com.sourcery.gymapp.workout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
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
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String force;

    @Size(max = 255)
    private String level;

    @Size(max = 255)
    private String mechanic;

    @Size(max = 255)
    private String equipment;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "primary_muscles", columnDefinition = "text[]")
    private List<String> primaryMuscles;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "secondary_muscles", columnDefinition = "text[]")
    private List<String> secondaryMuscles;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "description", columnDefinition = "text[]")
    private List<String> description;

    @Size(max = 255)
    private String category;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "images", columnDefinition = "text[]")
    private List<String> images;
}