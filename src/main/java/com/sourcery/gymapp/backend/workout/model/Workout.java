package com.sourcery.gymapp.backend.workout.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
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

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<WorkoutExercise> exercises = new ArrayList<>();

    public void setExercises(List<WorkoutExercise> exercises) {
        for (WorkoutExercise exercise : this.exercises) {
            exercise.setWorkout(null);
        }
        this.exercises.clear();

        for (WorkoutExercise exercise : exercises) {
            exercise.setWorkout(this);
        }
        this.exercises.addAll(exercises);
    }

    public void addExercise(WorkoutExercise exercise) {
        exercise.setWorkout(this);
        this.exercises.add(exercise);
    }

    public void removeExercise(WorkoutExercise exercise) {
        this.exercises.remove(exercise);
        exercise.setWorkout(null);
    }
}