package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.UserNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;
    private final AuditorAware<UUID> auditorAware;


    @Transactional
    public ResponseRoutineDto createRoutine(CreateRoutineDto routineDto) {
        UUID currentUserId = auditorAware.getCurrentAuditor().orElseThrow(
                () -> new UserNotFoundException("Current user is not authenticated")
        );

        Routine routine = routineMapper.toEntity(routineDto, currentUserId);

        routineRepository.save(routine);

        return routineMapper.toDto(routine);
    }


    public ResponseRoutineDto getRoutineById(UUID routineId) {
        Routine routine = findRoutineById(routineId);

        return routineMapper.toDto(routine);
    }

    public List<ResponseRoutineDto> getRoutinesByUserId() {
        UUID currentUserId = auditorAware.getCurrentAuditor().orElseThrow(
                () -> new UserNotFoundException("Current user is not authenticated")
        );

        List<Routine> routines = routineRepository.getRoutinesByUserId(currentUserId);

        return routines.stream()
                .map(routineMapper::toDto)
                .toList();
    }

    @Transactional
    public ResponseRoutineDto updateRoutine(UUID routineId, CreateRoutineDto routineDto) {
        Routine routine = findRoutineById(routineId);

        routineMapper.updateEntity(routine, routineDto);

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
