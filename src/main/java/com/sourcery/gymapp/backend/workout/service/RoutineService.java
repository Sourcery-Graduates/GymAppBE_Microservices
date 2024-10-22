package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.RoutineGridDto;
import com.sourcery.gymapp.backend.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.UserNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;
    private final WorkoutCurrentUserService currentUserService;

    @Transactional
    public ResponseRoutineDto createRoutine(CreateRoutineDto routineDto) {
        UUID currentUserId = currentUserService.getCurrentUserId();
        if (currentUserId == null) {
            throw new UserNotFoundException();
        }
        Routine routine = routineMapper.toEntity(routineDto, currentUserId);

        routineRepository.save(routine);

        return routineMapper.toDto(routine);
    }


    public ResponseRoutineDto getRoutineById(UUID routineId) {
        Routine routine = findRoutineById(routineId);

        return routineMapper.toDto(routine);
    }

    public List<ResponseRoutineDto> getRoutinesByUserId() {
        UUID currentUserId = currentUserService.getCurrentUserId();

        List<Routine> routines = routineRepository.findByUserId(currentUserId);

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

        return routineRepository.findById(id).orElseThrow(() -> new RoutineNotFoundException(id));
    }

    public RoutineGridDto searchRoutines(String name, Pageable pageable) {
        if (name == null) {
            return getAllRoutines(pageable);
        }

        Page<Routine> page = routineRepository.findByNameIgnoreCaseContaining(name, pageable);

        return new RoutineGridDto(
                page.getTotalPages(),
                page.getContent().stream().map(routineMapper::toDto).toList());
    }

    private RoutineGridDto getAllRoutines(Pageable pageable) {
        Page<Routine> page = routineRepository.findAll(pageable);
        List<ResponseRoutineDto> routines = page.map(routineMapper::toDto).getContent();

        return new RoutineGridDto(page.getTotalPages(), routines);
    }
}
