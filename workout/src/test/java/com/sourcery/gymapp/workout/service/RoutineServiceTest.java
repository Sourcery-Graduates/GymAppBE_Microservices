package com.sourcery.gymapp.workout.service;

import com.sourcery.gymapp.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.workout.dto.RoutinePageDto;
import com.sourcery.gymapp.workout.dto.RoutineWithLikeStatusProjection;
import com.sourcery.gymapp.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.workout.exception.UserNotFoundException;
import com.sourcery.gymapp.workout.factory.RoutineFactory;
import com.sourcery.gymapp.workout.exception.UserNotAuthorizedException;
import com.sourcery.gymapp.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.repository.RoutineRepository;
import com.sourcery.gymapp.workout.service.RoutineService;
import com.sourcery.gymapp.workout.service.WorkoutCurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoutineServiceTest {

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private RoutineMapper routineMapper;

    @Mock
    private WorkoutCurrentUserService currentUserService;

    @InjectMocks
    private RoutineService routineService;

    private UUID userId;
    private UUID routineId;
    private Routine routine;
    private CreateRoutineDto createRoutineDto;
    private ResponseRoutineDto responseRoutineDto;

    @BeforeEach
    void setUp() {
        routine = RoutineFactory.createRoutine();
        routineId = routine.getId();
        userId = routine.getUserId();
        createRoutineDto = RoutineFactory.createRoutineDto();
        responseRoutineDto = RoutineFactory.createResponseRoutineDto();
    }


    @Nested
    @DisplayName("Create Routine Tests")
    public class CreateRoutineTests {

        @Test
        void shouldCreateRoutineSuccessfully() {
            // Arrange
            when(currentUserService.getCurrentUserId()).thenReturn(userId);

            when(routineMapper.toEntity(createRoutineDto, userId)).thenReturn(routine);
            when(routineRepository.save(routine)).thenReturn(routine);
            when(routineMapper.toDto(routine)).thenReturn(responseRoutineDto);

            // Act
            ResponseRoutineDto result = routineService.createRoutine(createRoutineDto);

            // Assert
            assertEquals(responseRoutineDto, result);
            verify(routineRepository, times(1)).save(routine);
        }

        @Test
        void shouldThrowExceptionWhenUserNotAuthenticated() {
            // Arrange
            when(currentUserService.getCurrentUserId()).thenReturn(null);

            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> routineService.createRoutine(createRoutineDto));
        }

    }

    @Nested
    @DisplayName("Get Routine Tests")
    public class GetRoutineTests {

        @Test
        void shouldGetRoutineByIdSuccessfully() {
            // Arrange
            RoutineWithLikeStatusProjection projection = mock(RoutineWithLikeStatusProjection.class);
            when(projection.getRoutine()).thenReturn(routine);
            when(projection.isLikedByCurrentUser()).thenReturn(true);

            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(routineRepository.findRoutineWithLikeStatusByRoutineId(routineId, userId)).thenReturn(projection);
            when(routineMapper.toDto(routine, true)).thenReturn(responseRoutineDto);

            // Act
            ResponseRoutineDto result = routineService.getRoutineById(routineId);

            // Assert
            assertEquals(responseRoutineDto, result);
            verify(routineRepository, times(1)).findRoutineWithLikeStatusByRoutineId(routineId, userId);
        }

        @Test
        void shouldThrowExceptionWhenRoutineNotFound() {
            // Arrange
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(routineRepository.findRoutineWithLikeStatusByRoutineId(routineId, userId)).thenReturn(null);

            // Act & Assert
            assertThrows(RoutineNotFoundException.class, () -> routineService.getRoutineById(routineId));
        }

        @Test
        void shouldGetRoutinesByUserIdSuccessfully() {
            // Arrange
            RoutineWithLikeStatusProjection projection = mock(RoutineWithLikeStatusProjection.class);
            when(projection.getRoutine()).thenReturn(routine);
            when(projection.isLikedByCurrentUser()).thenReturn(true);

            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(routineRepository.findRoutinesWithLikeStatusByUserId(userId)).thenReturn(List.of(projection));
            when(routineMapper.toDto(routine, true)).thenReturn(responseRoutineDto);

            // Act
            List<ResponseRoutineDto> result = routineService.getRoutinesByUserId();

            // Assert
            assertEquals(1, result.size());
            assertEquals(responseRoutineDto, result.getFirst());
            verify(routineRepository, times(1)).findRoutinesWithLikeStatusByUserId(userId);
        }
    }

    @Nested
    @DisplayName("Get Paged Routine Tests")
    public class GetPagedRoutineTests {
        private Routine routine2;
        private Routine routine3;
        private ResponseRoutineDto responseRoutineDto2;
        private ResponseRoutineDto responseRoutineDto3;
        private Pageable pageable;

        @BeforeEach
        void setup() {
            routine2 = RoutineFactory.createRoutine("Test Routine 2");
            routine3 = RoutineFactory.createRoutine("Test Routine 3");

            responseRoutineDto2 = RoutineFactory.createResponseRoutineDto();
            responseRoutineDto3 = RoutineFactory.createResponseRoutineDto();
            pageable = PageRequest.of(0, 20);
        }

        @Test
        void shouldGetAllPagedRoutinesSuccessfully() {
            // Arrange
            RoutineWithLikeStatusProjection projection = mock(RoutineWithLikeStatusProjection.class);
            RoutineWithLikeStatusProjection projection2 = mock(RoutineWithLikeStatusProjection.class);
            RoutineWithLikeStatusProjection projection3 = mock(RoutineWithLikeStatusProjection.class);
            when(projection.getRoutine()).thenReturn(routine);
            when(projection2.getRoutine()).thenReturn(routine2);
            when(projection3.getRoutine()).thenReturn(routine3);
            when(projection.isLikedByCurrentUser()).thenReturn(true);
            when(projection2.isLikedByCurrentUser()).thenReturn(true);
            when(projection3.isLikedByCurrentUser()).thenReturn(true);

            Page<RoutineWithLikeStatusProjection> mockPage
                    = new PageImpl<>(List.of(projection, projection2, projection3), pageable, 3);
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(routineRepository.findAllWithLikeStatus(userId, pageable)).thenReturn(mockPage);
            when(routineMapper.toDto(routine, true)).thenReturn(responseRoutineDto);
            when(routineMapper.toDto(routine2, true)).thenReturn(responseRoutineDto2);
            when(routineMapper.toDto(routine3, true)).thenReturn(responseRoutineDto3);

            // Act
            RoutinePageDto result = routineService.searchRoutines("", pageable);

            // Assert
            assertEquals(1, result.totalPages());
            assertEquals(3, result.totalElements());
            assertEquals(List.of(responseRoutineDto, responseRoutineDto2, responseRoutineDto3), result.data());
        }

        @Test
        void shouldGetFilteredPagedRoutinesSuccessfully() {
            // Arrange
            String searchName = "Test Routine";
            RoutineWithLikeStatusProjection projection = mock(RoutineWithLikeStatusProjection.class);
            when(projection.getRoutine()).thenReturn(routine);
            when(projection.isLikedByCurrentUser()).thenReturn(true);

            Page<RoutineWithLikeStatusProjection> mockPage = new PageImpl<>(List.of(projection), pageable, 1);
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(routineRepository.findRoutinesWithLikeStatusByName(userId, searchName, pageable)).thenReturn(mockPage);
            when(routineMapper.toDto(routine, true)).thenReturn(responseRoutineDto);

            // Act
            RoutinePageDto result = routineService.searchRoutines(searchName, pageable);

            // Assert
            assertEquals(1, result.totalPages());
            assertEquals(1, result.totalElements());
            assertEquals(List.of(responseRoutineDto), result.data());
        }
    }

    @Nested
    @DisplayName("Update Routine Tests")
    public class UpdateRoutineTests {

        @Test
        void shouldUpdateRoutineSuccessfully() {
            // Arrange
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
            doNothing().when(routineMapper).updateEntity(routine, createRoutineDto);
            when(routineRepository.save(routine)).thenReturn(routine);
            when(routineMapper.toDto(routine)).thenReturn(responseRoutineDto);

            // Act
            ResponseRoutineDto result = routineService.updateRoutine(routineId, createRoutineDto);

            // Assert
            assertEquals(responseRoutineDto, result);
        }

        @Test
        void shouldThrowExceptionWhenUserNotAuthorized() {
            // Arrange
            when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));

            // Act & Assert
            assertThrows(UserNotAuthorizedException.class, () -> routineService.updateRoutine(routineId, createRoutineDto));
        }

        @Test
        void shouldThrowExceptionWhenRoutineNotFound() {
            // Arrange
            when(routineRepository.findById(routineId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RoutineNotFoundException.class, () -> routineService.updateRoutine(routineId, createRoutineDto));
        }
    }

    @Nested
    @DisplayName("Delete Routine Tests")
    public class DeleteRoutineTests {

        @Test
        void shouldDeleteRoutineSuccessfully() {
            // Arrange
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
            doNothing().when(routineRepository).delete(routine);

            // Act
            routineService.deleteRoutine(routineId);

            // Assert
            verify(routineRepository, times(1)).delete(routine);
        }

        @Test
        void shouldThrowExceptionWhenUserNotAuthorized() {
            // Arrange
            when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));

            // Act & Assert
            assertThrows(UserNotAuthorizedException.class, () -> routineService.deleteRoutine(routineId));
            verify(routineRepository, times(0)).delete(routine);
        }
    }
}
