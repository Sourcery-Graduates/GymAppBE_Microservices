package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.RoutinePageDto;
import com.sourcery.gymapp.backend.workout.dto.RoutineWithLikeStatusProjection;
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

// TODO: each pageable routine should be returned with isLikedByCurrentUser
@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;
    private final WorkoutCurrentUserService currentUserService;

    @Transactional
    public ResponseRoutineDto createRoutine(CreateRoutineDto createRoutineDto) {
        UUID currentUserId = currentUserService.getCurrentUserId();
        if (currentUserId == null) {
            throw new UserNotFoundException();
        }
        Routine routine = routineMapper.toEntity(createRoutineDto, currentUserId);

        routine = routineRepository.save(routine);

        return routineMapper.toDto(routine);
    }


    public ResponseRoutineDto getRoutineById(UUID routineId) {
        UUID currentUserId = currentUserService.getCurrentUserId();
        RoutineWithLikeStatusProjection projection =
                routineRepository.findRoutineWithLikeStatusByRoutineId(routineId, currentUserId);

        if (projection == null) {
            throw new RoutineNotFoundException(routineId);
        }

        return routineMapper.toDto(projection.getRoutine(), projection.isLikedByCurrentUser());
    }

    public List<ResponseRoutineDto> getRoutinesByUserId() {
        UUID currentUserId = currentUserService.getCurrentUserId();
        List<RoutineWithLikeStatusProjection> projections =
                routineRepository.findRoutinesWithLikeStatusByUserId(currentUserId);

        return projections.stream()
                .map(projection -> routineMapper.toDto(projection.getRoutine(), projection.isLikedByCurrentUser()))
                .toList();
    }

    @Transactional
    public ResponseRoutineDto updateRoutine(UUID routineId, CreateRoutineDto updateRoutineDto) {
        UUID currentUserId = currentUserService.getCurrentUserId();
        Routine routine = findRoutineById(routineId);

        checkIsUserAuthorized(currentUserId, routine.getUserId());

        routineMapper.updateEntity(routine, updateRoutineDto);

        routine = routineRepository.save(routine);

        return routineMapper.toDto(routine);
    }

    @Transactional
    public void deleteRoutine(UUID id) {
        UUID currentUserId = currentUserService.getCurrentUserId();
        Routine routine = findRoutineById(id);

        checkIsUserAuthorized(currentUserId, routine.getUserId());

        routineRepository.delete(routine);
    }

    public Routine findRoutineById(UUID routineId) {
        return routineRepository.findById(routineId).orElseThrow(() -> new RoutineNotFoundException(routineId));
    }

    @Transactional(readOnly = true)
    public RoutinePageDto searchRoutines(String name, Pageable pageable) {
        UUID currentUserId = currentUserService.getCurrentUserId();
        Page<RoutineWithLikeStatusProjection> routinePage = name == null || name.isBlank()
                ? routineRepository.findAllWithLikeStatus(currentUserId, pageable)
                : routineRepository.findRoutinesWithLikeStatusByName(currentUserId, name, pageable);

        List<ResponseRoutineDto> routines = routinePage.getContent().stream()
                .map(projection -> routineMapper.toDto(projection.getRoutine(), projection.isLikedByCurrentUser()))
                .toList();

        return new RoutinePageDto(
                routinePage.getTotalPages(),
                routinePage.getTotalElements(),
                routines
        );
    }

    private void checkIsUserAuthorized(UUID currentUserId, UUID routineUserId) {
        if (!routineUserId.equals(currentUserId)) {
            throw new UserNotAuthorizedException();
        }
    }
}
