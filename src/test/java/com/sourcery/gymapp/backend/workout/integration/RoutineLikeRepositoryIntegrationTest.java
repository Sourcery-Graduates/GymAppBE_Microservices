package com.sourcery.gymapp.backend.workout.integration;

import com.sourcery.gymapp.backend.workout.factory.RoutineFactory;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.model.routine_like.RoutineLike;
import com.sourcery.gymapp.backend.workout.repository.RoutineLikeRepository;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RoutineLikeRepositoryIntegrationTest extends BaseWorkoutIntegrationJPATest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RoutineLikeRepository routineLikeRepository;

    @Autowired
    private RoutineRepository routineRepository;

    private UUID userId;
    private UUID routineId;

    @BeforeEach
    void beforeEach() {
        userId = UUID.randomUUID();
        Routine routine = RoutineFactory.createRoutine();
        routine = routineRepository.save(routine);
        routineId = routine.getId();
    }

    @Test
    void testInsertIfNotExists() {
        Optional<UUID> firstInsert = routineLikeRepository.insertIfNotExists(routineId, userId);

        assertThat(firstInsert).isPresent();
        assertThat(firstInsert.get()).isEqualTo(routineId);
    }

    @Test
    void testDeleteByRoutineIdAndUserId() {
        routineLikeRepository.insertIfNotExists(routineId, userId);

        int deletedRows = routineLikeRepository.deleteByRoutineIdAndUserId(routineId, userId);

        assertThat(deletedRows).isEqualTo(1);
    }

    @Test
    void testInsertIfExists() {
        routineLikeRepository.insertIfNotExists(routineId, userId);

        Optional<UUID> secondInsert = routineLikeRepository.insertIfNotExists(routineId, userId);

        assertThat(secondInsert).isEmpty();
    }

    @Test
    void testDeleteNonExistentRecord() {
        int deletedRows = routineLikeRepository.deleteByRoutineIdAndUserId(routineId, userId);

        assertThat(deletedRows).isEqualTo(0);
    }

    /**
     * Tests cascade delete behavior for RoutineLikes when a Routine is deleted.
     * This test ensures that:
     * 1. RoutineLikes are correctly deleted at the database level via ON DELETE CASCADE.
     * 2. EntityManager.flush() and EntityManager.clear() are required to synchronize
     *    Hibernate's first-level cache with the database state.
     */
    @Test
    void testCascadeDeleteRoutine() {
        UUID userId2 = UUID.randomUUID();
        routineLikeRepository.insertIfNotExists(routineId, userId);
        routineLikeRepository.insertIfNotExists(routineId, userId2);

        assertThat(routineLikeRepository.findAll().size()).isEqualTo(2);

        routineRepository.deleteById(routineId);
        entityManager.flush();
        entityManager.clear();

        List<RoutineLike> likes = routineLikeRepository.findAll();
        assertThat(likes.isEmpty()).isTrue();
    }
}
