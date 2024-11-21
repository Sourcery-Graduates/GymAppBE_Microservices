package com.sourcery.gymapp.backend.workout.integration;

import com.sourcery.gymapp.backend.workout.factory.RoutineFactory;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

class RoutineRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RoutineRepository routineRepository;

    @Test
    @WithMockUser(username = "test_user")
    void testInsertIfNotExists() {
        Routine routine = RoutineFactory.createRoutine();

        Optional<Routine> routine1 = Optional.of(routineRepository.save(routine));
        assertThat(routine1).isPresent();
    }
}
