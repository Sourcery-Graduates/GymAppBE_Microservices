package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.exception.LikeAlreadyExistsException;
import com.sourcery.gymapp.backend.workout.exception.LikeNotFoundException;
import com.sourcery.gymapp.backend.workout.repository.RoutineLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineLikeService {

    private final RoutineLikeRepository routineLikeRepository;
    private final WorkoutCurrentUserService currentUserService;

    @Transactional
    public void addLikeToRoutine(UUID routineId) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        routineLikeRepository.insertIfNotExists(routineId, currentUserId)
                .orElseThrow(() -> new LikeAlreadyExistsException(routineId, currentUserId));
    }

    @Transactional
    public void removeLikeFromRoutine(UUID routineId) {
        UUID currentUserId = currentUserService.getCurrentUserId();
        int deletedCount = routineLikeRepository.deleteByRoutineIdAndUserId(routineId, currentUserId);
        if (deletedCount == 0) {
            throw new LikeNotFoundException(routineId, currentUserId);
        }
    }
}
