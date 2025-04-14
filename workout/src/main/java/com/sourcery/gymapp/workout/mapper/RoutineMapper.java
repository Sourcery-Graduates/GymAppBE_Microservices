package com.sourcery.gymapp.workout.mapper;

import com.sourcery.gymapp.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineSimpleDto;
import com.sourcery.gymapp.workout.model.Routine;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoutineMapper {
    public ResponseRoutineDto toDto(Routine routine) {
        return this.toDto(routine, false);
    }

    public ResponseRoutineSimpleDto toSimpleDto(Routine routine) {
        return new ResponseRoutineSimpleDto(
                routine.getId(),
                routine.getName()
        );
    }

    public ResponseRoutineDto toDto(Routine routine, boolean likedByCurrentUser) {
        return new ResponseRoutineDto(
                routine.getId(),
                routine.getName(),
                routine.getDescription(),
                routine.getCreatedAt(),
                routine.getUserId(),
                routine.getLikesCount(),
                likedByCurrentUser
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
