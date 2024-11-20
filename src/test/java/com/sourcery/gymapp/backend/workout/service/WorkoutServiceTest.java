package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.CreateWorkoutDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseWorkoutDto;
import com.sourcery.gymapp.backend.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.UserNotAuthorizedException;
import com.sourcery.gymapp.backend.workout.exception.UserNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.WorkoutNotFoundException;
import com.sourcery.gymapp.backend.workout.factory.WorkoutFactory;
import com.sourcery.gymapp.backend.workout.mapper.WorkoutMapper;
import com.sourcery.gymapp.backend.workout.model.Workout;
import com.sourcery.gymapp.backend.workout.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private RoutineService routineService;

    @Mock
    private WorkoutCurrentUserService currentUserService;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private WorkoutMapper workoutMapper;

    @Mock
    private WorkoutExerciseService workoutExerciseService;

    @InjectMocks
    private WorkoutService workoutService;

    private UUID userId;
    private UUID workoutId;
    private Workout workout;
    private CreateWorkoutDto createWorkoutDto;
    private ResponseWorkoutDto responseWorkoutDto;

    @BeforeEach
    void setUp() {
        workout = WorkoutFactory.createWorkout();
        workoutId = workout.getId();
        userId = workout.getUserId();
        createWorkoutDto = WorkoutFactory.createCreateWorkoutDto();
        responseWorkoutDto = WorkoutFactory.createResponseWorkoutDto();
    }

    @Nested
    @DisplayName("Create Workout Tests")
    public class CreateWorkoutTests {

        @Test
        void shouldCreateWorkoutSuccessfully() {
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(workoutMapper.toEntity(createWorkoutDto, userId, null, null, new HashMap<>())).thenReturn(workout);
            when(workoutRepository.save(workout)).thenReturn(workout);
            when(workoutMapper.toDto(workout)).thenReturn(responseWorkoutDto);

            ResponseWorkoutDto response = workoutService.createWorkout(createWorkoutDto);

            assertEquals(responseWorkoutDto, response);
            verify(workoutRepository, times(1)).save(workout);
        }

        @Test
        void shouldThrowExceptionWhenUserNotAuthenticated() {
            when(currentUserService.getCurrentUserId()).thenReturn(null);

            assertThrows(UserNotFoundException.class, () -> workoutService.createWorkout(createWorkoutDto));
        }

        @Test
        void shouldThrowExceptionWhenBasedOnWorkoutNotFound() {
            createWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                    "Test Name",
                    new Date(124, Calendar.JANUARY, 1),
                    "Test Comment",
                    null,
                    workoutId
            );
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

            assertThrows(WorkoutNotFoundException.class, () -> workoutService.createWorkout(createWorkoutDto));
        }

        @Test
        void shouldThrowExceptionWhenRoutineNotFound() {
            UUID routineId = UUID.randomUUID();
            createWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                    "Test Name",
                    new Date(124, Calendar.JANUARY, 1),
                    "Test Comment",
                    routineId,
                    null
            );
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(routineService.findRoutineById(routineId)).thenThrow(new RoutineNotFoundException(routineId));

            assertThrows(RoutineNotFoundException.class, () -> workoutService.createWorkout(createWorkoutDto));
        }
    }

    @Nested
    @DisplayName("Update Workout Tests")
    public class UpdateWorkoutTests {

        @Test
        void shouldUpdateWorkoutSuccessfully() {
            CreateWorkoutDto updateWorkoutDto = WorkoutFactory.createCreateWorkoutDto(
                    "Updated Name",
                    new Date(124, Calendar.JANUARY, 2),
                    "Updated Comment",
                    List.of()
            );
            when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            doNothing().when(workoutExerciseService).updateWorkoutExercises(updateWorkoutDto, workout);
            when(workoutRepository.save(workout)).thenReturn(workout);
            when(workoutMapper.toDto(workout)).thenReturn(responseWorkoutDto);

            ResponseWorkoutDto result = workoutService.updateWorkout(updateWorkoutDto, workoutId);

            assertEquals(responseWorkoutDto, result);
            assertAll(
                    () -> assertEquals(workout.getName(), updateWorkoutDto.name()),
                    () -> assertEquals(workout.getDate(), updateWorkoutDto.date()),
                    () -> assertEquals(workout.getComment(), updateWorkoutDto.comment())
            );
            verify(workoutRepository, times(1)).save(workout);
        }

        @Test
        void shouldThrowExceptionWhenUserNotAuthorized() {
            when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
            when(currentUserService.getCurrentUserId()).thenReturn(UUID.randomUUID());

            assertThrows(UserNotAuthorizedException.class, () -> workoutService.updateWorkout(createWorkoutDto, workoutId));
        }

        @Test
        void shouldThrowExceptionWhenWorkoutNotFound() {
            when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

            assertThrows(WorkoutNotFoundException.class, () -> workoutService.updateWorkout(createWorkoutDto, workoutId));
        }
    }

    @Nested
    @DisplayName("Get Workout Tests")
    public class GetWorkoutTests {

        @Test
        void shouldGetWorkoutByIdSuccessfully() {
            when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
            when(workoutMapper.toDto(workout)).thenReturn(responseWorkoutDto);

            ResponseWorkoutDto result = workoutService.getWorkoutById(workoutId);

            assertEquals(responseWorkoutDto, result);
            verify(workoutRepository, times(1)).findById(workoutId);
        }

        @Test
        void shouldThrowExceptionWhenWorkoutNotFound() {
            when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

            assertThrows(WorkoutNotFoundException.class, () -> workoutService.getWorkoutById(workoutId));
        }

        @Test
        void shouldGetWorkoutsByUserIdSuccessfully() {
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(workoutRepository.findByUserId(userId)).thenReturn(List.of(workout));
            when(workoutMapper.toDto(workout)).thenReturn(responseWorkoutDto);

            List<ResponseWorkoutDto> result = workoutService.getWorkoutsByUserId();

            assertEquals(List.of(responseWorkoutDto), result);
        }

        @Test
        void shouldThrowExceptionWhenUserNotAuthenticated() {
            when(currentUserService.getCurrentUserId()).thenReturn(null);

            assertThrows(UserNotFoundException.class, () -> workoutService.getWorkoutsByUserId());
        }
    }

    @Nested
    @DisplayName("Delete Workout Tests")
    public class DeleteWorkoutTests {

        @Test
        void shouldDeleteWorkoutSuccessfully() {
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
            doNothing().when(workoutRepository).setBasedOnWorkoutToNull(workout);
            doNothing().when(workoutRepository).delete(workout);

            workoutService.deleteWorkout(workoutId);

            verify(workoutRepository, times(1)).delete(workout);
        }

        @Test
        void shouldThrowExceptionWhenUserNotAuthorized() {
            when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
            when(currentUserService.getCurrentUserId()).thenReturn(UUID.randomUUID());

            assertThrows(UserNotAuthorizedException.class, () -> workoutService.deleteWorkout(workoutId));
        }

        @Test
        void shouldThrowExceptionWhenWorkoutNotFound() {
            when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

            assertThrows(WorkoutNotFoundException.class, () -> workoutService.deleteWorkout(workoutId));
        }
    }
}
