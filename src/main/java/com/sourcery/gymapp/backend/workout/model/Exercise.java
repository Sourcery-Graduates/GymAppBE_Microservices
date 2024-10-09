package com.sourcery.gymapp.backend.workout.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

    @ElementCollection
    @Column(name = "primary_muscles", columnDefinition = "text[]")
    private List<String> primaryMuscles;

    @ElementCollection
    @Column(name = "secondary_muscles", columnDefinition = "text[]")
    private List<String> secondaryMuscles;

    private String description;

    private String category;

    @ElementCollection
    @Column(name = "images", columnDefinition = "text[]")
    private List<String> images;
}