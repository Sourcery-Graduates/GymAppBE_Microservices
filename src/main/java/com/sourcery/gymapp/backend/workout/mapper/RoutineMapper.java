package com.sourcery.gymapp.backend.workout.mapper;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.model.Routine;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoutineMapper {
    public ResponseRoutineDto toDto(Routine routine) {
        return new ResponseRoutineDto(
                routine.getId(),
                routine.getName(),
                routine.getDescription(),
                routine.getCreatedAt(),
                routine.getUserId(),
                routine.getLikesCount()
        );
    }

    public Routine toEntity(CreateRoutineDto routineDto, UUID userId) {
        Routine routine = new Routine();
        routine.setName(routineDto.name());
        routine.setDescription(routineDto.description());
        routine.setUserId(userId);

        return routine;
    }

    public void updateEntity(Routine routine, CreateRoutineDto routineDto) {
        routine.setName(routineDto.name());
        routine.setDescription(routineDto.description());
    }
}
