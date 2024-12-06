package com.sourcery.gymapp.backend.workout.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.sourcery.gymapp.backend.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class ExerciseRepositoryIntegrationTest extends BaseIntegrationJPATest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @BeforeEach
    void beforeEach() {
        exerciseRepository.deleteAll();

        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Arm Curl"));
        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Leg Press"));
        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Arm Extension"));
        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Chest Press"));
    }

    @Test
    @Transactional
    void testFindByPrefixOrContaining_MatchesPrefix() {
        String prefix = "Arm";
        Page<Exercise> result = exerciseRepository.findByPrefixOrContaining(prefix, Pageable.ofSize(10).withPage(0));

        assertThat(result.getContent())
                .extracting(Exercise::getName)
                .containsExactly("Arm Curl", "Arm Extension");
    }

    @Test
    void testFindByPrefixOrContaining_MatchesSubstring() {
        String prefix = "Press";
        Page<Exercise> result = exerciseRepository.findByPrefixOrContaining(prefix, Pageable.ofSize(10).withPage(0));

        assertThat(result.getContent())
                .extracting(Exercise::getName)
                .containsExactly("Chest Press", "Leg Press");
    }

    @Test
    void testFindByPrefixOrContaining_Prioritization() {
        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Leg Arm Workout"));

        String prefix = "Arm";
        Page<Exercise> result = exerciseRepository.findByPrefixOrContaining(prefix, Pageable.ofSize(10).withPage(0));

        assertThat(result.getContent())
                .extracting(Exercise::getName)
                .containsExactly("Arm Curl", "Arm Extension", "Leg Arm Workout");
    }

    @Test
    void testFindByPrefixOrContaining_EmptyPrefix() {
        String prefix = "";
        Page<Exercise> result = exerciseRepository.findByPrefixOrContaining(prefix, Pageable.ofSize(10).withPage(0));

        assertThat(result.getContent())
                .extracting(Exercise::getName)
                .containsExactly("Arm Curl", "Arm Extension", "Chest Press", "Leg Press");
    }

    @Test
    void testFindByPrefixOrContaining_NoMatch() {
        String prefix = "Nonexistent";
        Page<Exercise> result = exerciseRepository.findByPrefixOrContaining(prefix, Pageable.ofSize(10).withPage(0));

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void testFindByPrefixOrContaining_CaseInsensitive() {
        String prefix = "arm";
        Page<Exercise> result = exerciseRepository.findByPrefixOrContaining(prefix, Pageable.ofSize(10).withPage(0));

        assertThat(result.getContent())
                .extracting(Exercise::getName)
                .containsExactly("Arm Curl", "Arm Extension");
    }

    @Test
    void testFindByPrefixOrContaining_Pagination() {
        for (int i = 0; i < 21; i++) {
            exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Exercise " + i));
        }

        String prefix = "Exercise";
        Page<Exercise> result = exerciseRepository.findByPrefixOrContaining(prefix, Pageable.ofSize(10).withPage(1));

        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getNumber()).isEqualTo(1);
    }
}
