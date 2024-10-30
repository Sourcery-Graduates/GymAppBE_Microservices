package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.RoutinePageDto;
import com.sourcery.gymapp.backend.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.UserNotAuthorizedException;
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
        UUID currentUserId = currentUserService.getCurrentUserId();
        Routine routine = findRoutineById(routineId);

        checkIsUserAuthorized(currentUserId, routine.getUserId());

        routineMapper.updateEntity(routine, routineDto);

        routineRepository.save(routine);

        return routineMapper.toDto(routine);
    }

    @Transactional
    public void deleteRoutine(UUID id) {
        UUID currentUserId = currentUserService.getCurrentUserId();
        Routine routine = findRoutineById(id);

        checkIsUserAuthorized(currentUserId, routine.getUserId());

        routineRepository.delete(routine);
    }

    public Routine findRoutineById(UUID id) {

        return routineRepository.findById(id).orElseThrow(() -> new RoutineNotFoundException(id));
    }

    public RoutinePageDto searchRoutines(String name, Pageable pageable) {
        Page<Routine> routinePage;

        if (name == null || name.isBlank()) {
            routinePage = getAllRoutines(pageable);
        } else {
            routinePage = routineRepository.findByNameIgnoreCaseContaining(name, pageable);
        }

        List<ResponseRoutineDto> routines = routinePage.map(routineMapper::toDto).getContent();

        return new RoutinePageDto(
                routinePage.getTotalPages(),
                routinePage.getTotalElements(),
                routines
        );
    }

    private Page<Routine> getAllRoutines(Pageable pageable) {
        return routineRepository.findAll(pageable);
    }

    private void checkIsUserAuthorized(UUID currentUserId, UUID routineUserId) {

        if (routineUserId != currentUserId) {
            throw new UserNotAuthorizedException();
        }
    }
}
