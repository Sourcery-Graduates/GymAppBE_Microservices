package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;

    @Transactional
    public ResponseRoutineDto createRoutine(CreateRoutineDto routineDto) {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001"); //TODO get userId from security context
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

        return routines.stream()
                .map(routineMapper::toDto)
                .toList();
    }

    @Transactional
    public ResponseRoutineDto updateRoutine(UUID routineId, CreateRoutineDto routineDto) {
        Routine routine = findRoutineById(routineId);

        routine.setName(routineDto.name());
        routine.setDescription(routineDto.description());

        routineRepository.save(routine);

        return routineMapper.toDto(routine);
    }

    @Transactional
    public void deleteRoutine(UUID id) {
        Routine routine = findRoutineById(id);

        routineRepository.delete(routine);
    }

    private Routine findRoutineById(UUID id) {
        Routine routine = routineRepository.getRoutineById(id);

        if (routine == null) {
            throw new RoutineNotFoundException(id);
        }

        return routine;
    }
}
