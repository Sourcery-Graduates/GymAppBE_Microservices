package com.sourcery.gymapp.workout.service;

import com.sourcery.gymapp.backend.workout.exception.LikeAlreadyExistsException;
import com.sourcery.gymapp.backend.workout.exception.LikeNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.RoutineLikeMapper;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.producer.WorkoutKafkaProducer;
import com.sourcery.gymapp.backend.workout.repository.RoutineLikeRepository;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;

import static com.sourcery.gymapp.backend.workout.factory.RoutineFactory.createRoutine;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoutineLikeServiceTest {

    private RoutineLikeService routineLikeService;

    @Mock
    private RoutineLikeRepository routineLikeRepository;

    @Mock
    private WorkoutCurrentUserService currentUserService;

    @Mock
    private RoutineRepository routineRepository;

    private final RoutineLikeMapper routineLikeMapper = new RoutineLikeMapper();

    @Mock
    private WorkoutKafkaProducer workoutKafkaProducer;

    @Mock
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        routineLikeService = new RoutineLikeService(
                routineLikeRepository,
                currentUserService,
                routineRepository,
                routineLikeMapper,
                workoutKafkaProducer,
                transactionTemplate
        );
    }

    @Test
    void addLikeToRoutine_ShouldSucceed_WhenLikeDoesNotExist() {
        // Arrange
        UUID routineId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        Routine routine = createRoutine("Test routine", routineId, currentUserId);


        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(routineLikeRepository.insertIfNotExists(routineId, currentUserId))
                .thenReturn(Optional.of(UUID.randomUUID()));
        when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            return ((TransactionCallback<?>) invocation.getArgument(0)).doInTransaction(null);
        });

        // Act & Assert
        assertDoesNotThrow(() -> routineLikeService.addLikeToRoutine(routineId));
        verify(routineLikeRepository, times(1)).insertIfNotExists(routineId, currentUserId);
        verify(workoutKafkaProducer, times(1)).sendRoutineLikeEvent(any());
    }

    @Test
    void addLikeToRoutine_ShouldThrowLikeAlreadyExistsException_WhenLikeExists() {
        // Arrange
        UUID routineId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        Routine routine = createRoutine("Test routine", routineId, currentUserId);

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(routineLikeRepository.insertIfNotExists(routineId, currentUserId)).thenReturn(Optional.empty());
        when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            return ((TransactionCallback<?>) invocation.getArgument(0)).doInTransaction(null);
        });


        // Act & Assert
        LikeAlreadyExistsException exception = assertThrows(
                LikeAlreadyExistsException.class,
                () -> routineLikeService.addLikeToRoutine(routineId)
        );
        assertEquals("The like for routine with ID [%s] already exists by user with ID [%s]".formatted(routineId, currentUserId), exception.getMessage());
        verify(routineLikeRepository, times(1)).insertIfNotExists(routineId, currentUserId);
        verify(workoutKafkaProducer, times(0)).sendRoutineLikeEvent(any());
    }

    @Test
    void addLikeToRoutine_ShouldThrowRoutineNotFoundException_WhenRoutineDoesNotExist() {
        UUID routineId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(routineLikeRepository.insertIfNotExists(routineId, currentUserId)).thenReturn(Optional.of(UUID.randomUUID()));
        when(routineRepository.findById(routineId)).thenReturn(Optional.empty());
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            return ((TransactionCallback<?>) invocation.getArgument(0)).doInTransaction(null);
        });

        assertThrows(
                RoutineNotFoundException.class,
                () -> routineLikeService.addLikeToRoutine(routineId)
        );
        verify(routineLikeRepository, times(0)).insertIfNotExists(routineId, currentUserId);
        verify(workoutKafkaProducer, times(0)).sendRoutineLikeEvent(any());
    }

    @Test
    void removeLikeFromRoutine_ShouldSucceed_WhenLikeExists() {
        // Arrange
        UUID routineId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        Routine routine = createRoutine("Test routine", routineId, currentUserId);

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(routineLikeRepository.deleteByRoutineIdAndUserId(routineId, currentUserId)).thenReturn(1);
        when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            return ((TransactionCallback<?>) invocation.getArgument(0)).doInTransaction(null);
        });

        // Act & Assert
        assertDoesNotThrow(() -> routineLikeService.removeLikeFromRoutine(routineId));
        verify(routineLikeRepository, times(1)).deleteByRoutineIdAndUserId(routineId, currentUserId);
        verify(workoutKafkaProducer, times(1)).sendRoutineLikeEvent(any());
    }

    @Test
    void removeLikeFromRoutine_ShouldThrowLikeNotFoundException_WhenLikeDoesNotExist() {
        // Arrange
        UUID routineId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        Routine routine = createRoutine("Test routine", routineId, currentUserId);

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(routineLikeRepository.deleteByRoutineIdAndUserId(routineId, currentUserId)).thenReturn(0);
        when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            return ((TransactionCallback<?>) invocation.getArgument(0)).doInTransaction(null);
        });

        // Act & Assert
        LikeNotFoundException exception = assertThrows(
                LikeNotFoundException.class,
                () -> routineLikeService.removeLikeFromRoutine(routineId)
        );
        assertEquals("The like for routine with ID [%s] not found by user with ID [%s]".formatted(routineId, currentUserId), exception.getMessage());
        verify(routineLikeRepository, times(1)).deleteByRoutineIdAndUserId(routineId, currentUserId);
        verify(workoutKafkaProducer, times(0)).sendRoutineLikeEvent(any());
    }

    @Test
    void removeLikeFromRoutine_ShouldThrowRoutineNotFoundException_WhenRoutineDoesNotExist() {
        UUID routineId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(routineLikeRepository.deleteByRoutineIdAndUserId(routineId, currentUserId)).thenReturn(0);
        when(routineRepository.findById(routineId)).thenReturn(Optional.empty());
        when(transactionTemplate.execute(any())).thenAnswer(invocation -> {
            return ((TransactionCallback<?>) invocation.getArgument(0)).doInTransaction(null);
        });

        assertThrows(
                RoutineNotFoundException.class,
                () -> routineLikeService.removeLikeFromRoutine(routineId)
        );
        verify(routineLikeRepository, times(0)).deleteByRoutineIdAndUserId(routineId, currentUserId);
        verify(workoutKafkaProducer, times(0)).sendRoutineLikeEvent(any());
    }
}
