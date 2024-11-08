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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "workout_exercise", schema = "workout_data")
public class WorkoutExercise extends BaseEntity {

    @Column(nullable = false)
    private Integer orderNumber;

    @Size(max = 255)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id", nullable = false)
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "id", nullable = false)
    private Workout workout;

    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<WorkoutExerciseSet> sets = new ArrayList<>();

    public void setSets(List<WorkoutExerciseSet> sets) {
        for (WorkoutExerciseSet set : this.sets) {
            set.setWorkoutExercise(null);
        }
        this.sets.clear();

        for (WorkoutExerciseSet set : sets) {
            set.setWorkoutExercise(this);
        }
        this.sets.addAll(sets);
    }

    public void addSet(WorkoutExerciseSet set) {
        this.sets.add(set);
        set.setWorkoutExercise(this);
    }

    public void removeSet(WorkoutExerciseSet set) {
        this.sets.remove(set);
        set.setWorkoutExercise(null);
    }
}