package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;

    public ResponseRoutineDto createRoutine(CreateRoutineDto routineDto) {
        UUID userId = UUID.fromString("4012527c-334e-4605-aa8e-1fef26ea37a5"); //TODO get userId from security context
        Routine routine = routineMapper.toEntity(routineDto, userId);

        routineRepository.save(routine);

        return routineMapper.toDto(routine);
    }


    public ResponseRoutineDto getRoutineById(UUID routineId) {
        Routine routine = findRoutineById(routineId);

        return routineMapper.toDto(routine);
    }

    public List<ResponseRoutineDto> getRoutinesByUserId(UUID userId) {
        List<Routine> routines = routineRepository.getRoutinesByUserId(userId);

        if (routines.isEmpty()) {
            throw new RuntimeException("No routines found"); //TODO create custom exceptions
        }

        return routines.stream()
                .map(routineMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseRoutineDto updateRoutine(UUID routineId, CreateRoutineDto routineDto) {
        Routine routine = findRoutineById(routineId);

        routine.setName(routineDto.name());
        routine.setDescription(routineDto.description());

        routineRepository.save(routine);

        return routineMapper.toDto(routine);
    }

    public void deleteRoutine(UUID id) {
        Routine routine = findRoutineById(id);

        routineRepository.delete(routine);
    }

    private Routine findRoutineById(UUID id) {
        Routine routine = routineRepository.getRoutineById(id);

        if (routine == null) {
            throw new RuntimeException("Routine not found"); //TODO create custom exceptions
        }

        return routine;
    }
}
