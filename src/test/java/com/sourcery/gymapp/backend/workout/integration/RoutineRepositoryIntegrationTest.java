package com.sourcery.gymapp.backend.workout.integration;

import com.sourcery.gymapp.backend.workout.dto.RoutineWithLikeStatusProjection;
import com.sourcery.gymapp.backend.workout.factory.RoutineFactory;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import com.sourcery.gymapp.backend.workout.repository.RoutineLikeRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RoutineRepositoryIntegrationTest extends BaseIntegrationJPATest {

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private RoutineLikeRepository routineLikeRepository;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        routineRepository.deleteAll();
        routineLikeRepository.deleteAll();

        // Create test routines
        routineRepository.save(RoutineFactory.createRoutine("Routine A", "Test Description A",
                LocalDateTime.now(), userId, 10L));
        routineRepository.save(RoutineFactory.createRoutine("Routine B", "Test Description B", LocalDateTime.now(), userId, 5L));
        routineRepository.save(RoutineFactory.createRoutine("Routine C", "Test Description C", LocalDateTime.now(), UUID.randomUUID(), 2L));
    }

    @Test
    void testFindRoutineWithLikeStatusByRoutineId() {
        Routine routine = routineRepository.findAll().getFirst();
        UUID routineId = routine.getId();

        // Add a like for the routine by the test user
        routineLikeRepository.insertIfNotExists(routineId, userId);

        RoutineWithLikeStatusProjection result = routineRepository.findRoutineWithLikeStatusByRoutineId(routineId, userId);

        assertThat(result).isNotNull();
        assertThat(result.getRoutine().getId()).isEqualTo(routineId);
        assertThat(result.isLikedByCurrentUser()).isTrue();
    }

    @Test
    void testFindRoutinesWithLikeStatusByUserId() {
        Routine routineA = routineRepository.findAll().stream()
                .filter(r -> r.getName().equals("Routine A"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Routine A not found"));

        routineLikeRepository.insertIfNotExists(routineA.getId(), userId);

        List<RoutineWithLikeStatusProjection> results = routineRepository.findRoutinesWithLikeStatusByUserId(userId);

        assertThat(results).hasSize(2);

        // Identify "Routine A" in the results and verify its like status
        RoutineWithLikeStatusProjection likedRoutine = results.stream()
                .filter(r -> r.getRoutine().getName().equals("Routine A"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Liked Routine A not found"));

        assertThat(likedRoutine.isLikedByCurrentUser()).isTrue();
    }


    @Test
    void testFindAllWithLikeStatus() {
        Routine routine = routineRepository.findAll().stream()
                .filter(r -> r.getName().equals("Routine A"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Routine A not found"));
        routineLikeRepository.insertIfNotExists(routine.getId(), userId);

        Pageable pageable = PageRequest.of(0, 10);
        Page<RoutineWithLikeStatusProjection> result = routineRepository.findAllWithLikeStatus(userId, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);

        RoutineWithLikeStatusProjection firstResult = result.getContent().stream()
                .filter(r -> r.getRoutine().getName().equals("Routine A"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Liked Routine A not found"));

        assertThat(firstResult.isLikedByCurrentUser()).isTrue();
    }


    @Test
    void testFindRoutinesWithLikeStatusByName() {
        // Add a routine with a unique name
        routineRepository.save(RoutineFactory.createRoutine("Yoga Routine", "Relaxing yoga session"));

        Pageable pageable = PageRequest.of(0, 10);
        Page<RoutineWithLikeStatusProjection> result = routineRepository.findRoutinesWithLikeStatusByName(userId, "Yoga", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getRoutine().getName()).isEqualTo("Yoga Routine");
    }

    @Test
    void testFindRoutinesWithLikeStatusByName_NoMatches() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoutineWithLikeStatusProjection> result = routineRepository.findRoutinesWithLikeStatusByName(userId, "Nonexistent", pageable);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void testFindAllWithLikeStatus_EmptyDatabase() {
        routineRepository.deleteAll();

        Pageable pageable = PageRequest.of(0, 10);
        Page<RoutineWithLikeStatusProjection> result = routineRepository.findAllWithLikeStatus(userId, pageable);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void testFindRoutineWithLikeStatusByRoutineId_NoLikes() {
        Routine routine = routineRepository.findAll().getFirst();
        UUID routineId = routine.getId();

        RoutineWithLikeStatusProjection result = routineRepository.findRoutineWithLikeStatusByRoutineId(routineId, userId);

        assertThat(result).isNotNull();
        assertThat(result.getRoutine().getId()).isEqualTo(routineId);
        assertThat(result.isLikedByCurrentUser()).isFalse();
    }

    @Test
    void testPaginationForFindAllWithLikeStatus() {
        // Add more routines to test pagination
        for (int i = 1; i <= 20; i++) {
            routineRepository.save(RoutineFactory.createRoutine("Routine " + i));
        }

        Pageable pageable = PageRequest.of(1, 10); // Second page
        Page<RoutineWithLikeStatusProjection> result = routineRepository.findAllWithLikeStatus(userId, pageable);

        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getNumber()).isEqualTo(1);
    }
}
