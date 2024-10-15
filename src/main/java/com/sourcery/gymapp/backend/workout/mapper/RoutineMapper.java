package com.sourcery.gymapp.backend.workout.mapper;

import com.sourcery.gymapp.backend.workout.dto.RoutineDto;
import com.sourcery.gymapp.backend.workout.model.Routine;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoutineMapper {
    public RoutineDto toDto(Routine routine) {
        return new RoutineDto(
            routine.getName(),
            routine.getDescription(),
            routine.getCreatedAt()
        );
    }

    public Routine toEntity(RoutineDto routineDto, UUID userId) {
        Routine routine = new Routine();
        routine.setId(UUID.randomUUID());
        routine.setName(routineDto.name());
        routine.setDescription(routineDto.description());
        routine.setUserId(userId);

        return routine;
    }
}
