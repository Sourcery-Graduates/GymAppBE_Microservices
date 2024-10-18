package com.sourcery.gymapp.backend.workout;


import com.sourcery.gymapp.backend.factory.RoutineFactory;
import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.backend.workout.model.Routine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RoutineMapperTest {

    @MockBean
    private RoutineMapper routineMapper;
    private Routine routine;
    private CreateRoutineDto createRoutineDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        routineMapper = new RoutineMapper();
        routine = RoutineFactory.createRoutine();
        userId = routine.getUserId();
        createRoutineDto = RoutineFactory.createRoutineDto();
    }

    @Test
    void shouldMapRoutineToResponseRoutineDto() {
        ResponseRoutineDto responseRoutineDto = routineMapper.toDto(routine);

        assertAll(
                () -> assertEquals(routine.getId(), responseRoutineDto.id()),
                () -> assertEquals(routine.getName(), responseRoutineDto.name()),
                () -> assertEquals(routine.getDescription(), responseRoutineDto.description()),
                () -> assertEquals(routine.getCreatedAt(), responseRoutineDto.createdAt())
        );
    }

    @Test
    void shouldMapCreateRoutineDtoToRoutine() {
        routine = routineMapper.toEntity(createRoutineDto, userId);

        assertAll(
                () -> assertEquals(createRoutineDto.name(), routine.getName()),
                () -> assertEquals(createRoutineDto.description(), routine.getDescription()),
                () -> assertEquals(userId, routine.getUserId())
        );
    }

    @Test
    void shouldUpdateRoutineFromCreateRoutineDto() {
        routineMapper.updateEntity(routine, createRoutineDto);

        assertAll(
                () -> assertEquals(createRoutineDto.name(), routine.getName()),
                () -> assertEquals(createRoutineDto.description(), routine.getDescription())
        );
    }
}
