package com.sourcery.gymapp.workout.service;

import com.sourcery.gymapp.workout.event.RoutineLikeEvent;
import com.sourcery.gymapp.workout.exception.LikeAlreadyExistsException;
import com.sourcery.gymapp.workout.exception.LikeNotFoundException;
import com.sourcery.gymapp.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.workout.mapper.RoutineLikeMapper;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.producer.WorkoutKafkaProducer;
import com.sourcery.gymapp.workout.repository.RoutineLikeRepository;
import com.sourcery.gymapp.workout.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineLikeService {

    private final RoutineLikeRepository routineLikeRepository;
    private final WorkoutCurrentUserService currentUserService;
    private final RoutineRepository routineRepository;
    private final RoutineLikeMapper routineLikeMapper;
    private final WorkoutKafkaProducer kafkaProducer;
    private final TransactionTemplate transactionTemplate;

    public void addLikeToRoutine(UUID routineId) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        RoutineLikeEvent event = transactionTemplate.execute(status -> {
            Routine routineCandidate = routineRepository.findById(routineId)
                    .orElseThrow(() -> new RoutineNotFoundException(routineId));

            routineLikeRepository.insertIfNotExists(routineId, currentUserId)
                    .orElseThrow(() -> new LikeAlreadyExistsException(routineId, currentUserId));

            return routineLikeMapper.toAddLikeEvent(currentUserId, routineCandidate);
        });

        if (event != null) {
            kafkaProducer.sendRoutineLikeEvent(event);
        }
    }

    public void removeLikeFromRoutine(UUID routineId) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        RoutineLikeEvent event = transactionTemplate.execute(status -> {
            Routine routineCandidate = routineRepository.findById(routineId)
                    .orElseThrow(() -> new RoutineNotFoundException(routineId));

            int deletedCount = routineLikeRepository.deleteByRoutineIdAndUserId(routineId, currentUserId);
            if (deletedCount == 0) {
                throw new LikeNotFoundException(routineId, currentUserId);
            }

            return routineLikeMapper.toRemoveLikeEvent(currentUserId, routineCandidate);
        });

        if (event != null) {
            kafkaProducer.sendRoutineLikeEvent(event);
        }
    }
}
