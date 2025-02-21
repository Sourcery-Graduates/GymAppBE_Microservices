package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.events.RoutineLikeEvent;
import com.sourcery.gymapp.backend.workout.exception.LikeAlreadyExistsException;
import com.sourcery.gymapp.backend.workout.exception.LikeNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.RoutineLikeMapper;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.repository.RoutineLikeRepository;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineLikeService {

    private final RoutineLikeRepository routineLikeRepository;
    private final WorkoutCurrentUserService currentUserService;
    private final RoutineRepository routineRepository;
    private final RoutineLikeMapper routineLikeMapper;

    @Transactional
    public void addLikeToRoutine(UUID routineId) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        Routine routineCandidate = routineRepository.findById(routineId)
                .orElseThrow(() -> new RoutineNotFoundException(routineId));

        routineLikeRepository.insertIfNotExists(routineId, currentUserId)
                .orElseThrow(() -> new LikeAlreadyExistsException(routineId, currentUserId));

        RoutineLikeEvent event = routineLikeMapper.toAddLikeEvent(currentUserId, routineCandidate);
        System.out.println(event);
    }

    @Transactional
    public void removeLikeFromRoutine(UUID routineId) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        Routine routineCandidate = routineRepository.findById(routineId)
                .orElseThrow(() -> new RoutineNotFoundException(routineId));

        int deletedCount = routineLikeRepository.deleteByRoutineIdAndUserId(routineId, currentUserId);
        if (deletedCount == 0) {
            throw new LikeNotFoundException(routineId, currentUserId);
        }

        RoutineLikeEvent event = routineLikeMapper.toRemoveLikeEvent(currentUserId, routineCandidate);
        System.out.println(event);
    }
}
