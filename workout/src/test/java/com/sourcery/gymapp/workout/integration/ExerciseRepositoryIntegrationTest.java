package com.sourcery.gymapp.workout.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.sourcery.gymapp.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.workout.model.Exercise;
import com.sourcery.gymapp.workout.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public class ExerciseRepositoryIntegrationTest extends BaseWorkoutIntegrationJPATest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @BeforeEach
    void beforeEach() {
        exerciseRepository.deleteAll();

        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Arm Curl", List.of("biceps")));
        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Leg Press", List.of("quads")));
        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Arm Extension", List.of("triceps")));
        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Chest Press", List.of("chest")));
        exerciseRepository.save(ExerciseFactory.createExercise(UUID.randomUUID(), "Bench Press", List.of("chest")));
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
                .containsExactly("Bench Press", "Chest Press", "Leg Press");
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
                .containsExactly("Arm Curl", "Arm Extension", "Bench Press", "Chest Press", "Leg Press");
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

    @Test
    void testFindAllByPrimaryMuscle_Chest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

        String primaryMuscle = "chest";
        String primaryMusclePrepared = '{' + primaryMuscle+ '}';

        Page<Exercise> result = exerciseRepository.findAllByPrimaryMuscle(primaryMusclePrepared, pageable);

        assertThat(result.getContent())
                .extracting(Exercise::getName)
                .containsExactly("Bench Press", "Chest Press");
    }

    @Test
    void testFindAllByPrimaryMuscle_NoMatch() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

        String primaryMuscle = "middle back";
        String primaryMusclePrepared = '{' + primaryMuscle+ '}';

        Page<Exercise> result = exerciseRepository.findAllByPrimaryMuscle(primaryMusclePrepared, pageable);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void testFindAllByPrimaryMuscleAndPrefix() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

        String primaryMuscle = "chest";
        String primaryMusclePrepared = '{' + primaryMuscle + '}';
        String prefix = "bench";

        Page<Exercise> result = exerciseRepository.findAllByPrimaryMuscleAndPrefix(primaryMusclePrepared, prefix, pageable);

        assertThat(result.getContent())
                .extracting(Exercise::getName)
                .containsExactly("Bench Press");
    }

    @Test
    void testFindAllByPrimaryMuscleAndPrefix_EmptyPrefix() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

        String primaryMuscle = "chest";
        String primaryMusclePrepared = '{' + primaryMuscle + '}';
        String prefix = "";
        Page<Exercise> result = exerciseRepository.findAllByPrimaryMuscleAndPrefix(primaryMusclePrepared, prefix, pageable);

        assertThat(result.getContent())
                .extracting(Exercise::getName)
                .containsExactly("Bench Press", "Chest Press");
    }
}
