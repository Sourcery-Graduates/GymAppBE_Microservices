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

/**
 * Service class for managing workout routines.
 * Handles business logic for routines including CRUD operations,
 * authorization checks, and data validation.
 */
// TODO: each pageable routine should be returned with isLikedByCurrentUser
@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;
    private final WorkoutCurrentUserService currentUserService;

    /**
     * Creates a new workout routine for the current user.
     *
     * @param createRoutineDto contains routine details to create
     * @return ResponseRoutineDto with created routine details
     * @throws UserNotFoundException if current user not found
     */
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

    /**
     * Updates an existing routine if user is authorized.
     *
     * @param routineId unique identifier of routine to update
     * @param updateRoutineDto contains updated routine details
     * @return ResponseRoutineDto with updated routine details
     * @throws RoutineNotFoundException if routine not found
     * @throws UserNotAuthorizedException if user not authorized
     */
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

    /**
     * Finds a routine by its ID.
     *
     * @param routineId unique identifier of routine to find
     * @return Routine entity if found
     * @throws RoutineNotFoundException if routine not found
     */
    public Routine findRoutineById(UUID routineId) {
        return routineRepository.findById(routineId).orElseThrow(() -> new RoutineNotFoundException(routineId));
    }

    /**
     * Searches for routines by name with pagination and like status.
     * If name is null or blank, returns all routines for the current user.
     * Includes information about whether each routine is liked by the current user.
     *
     * @param name search term for routine name, can be null or blank
     * @param pageable pagination and sorting parameters
     * @return RoutinePageDto containing paginated routines with like status
     * @throws UserNotFoundException if current user not found
     */
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

    /**
     * Verifies if the current user is authorized to modify the routine.
     * Used for update and delete operations.
     *
     * @param currentUserId ID of the current user
     * @param routineUserId ID of the user who owns the routine
     * @throws UserNotAuthorizedException if current user is not the owner
     */
    private void checkIsUserAuthorized(UUID currentUserId, UUID routineUserId) {
        if (!routineUserId.equals(currentUserId)) {
            throw new UserNotAuthorizedException();
        }
    }
}
