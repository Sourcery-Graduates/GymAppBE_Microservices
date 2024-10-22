package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.factory.RoutineFactory;
import com.sourcery.gymapp.backend.workout.dto.CreateRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.ResponseRoutineDto;
import com.sourcery.gymapp.backend.workout.dto.RoutineGridDto;
import com.sourcery.gymapp.backend.workout.exception.RoutineNotFoundException;
import com.sourcery.gymapp.backend.workout.exception.UserNotFoundException;
import com.sourcery.gymapp.backend.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.backend.workout.model.Routine;
import com.sourcery.gymapp.backend.workout.repository.RoutineRepository;
import com.sourcery.gymapp.backend.workout.service.RoutineService;
import com.sourcery.gymapp.backend.workout.service.WorkoutCurrentUserService;
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
            when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
            when(routineMapper.toDto(routine)).thenReturn(responseRoutineDto);

            // Act
            ResponseRoutineDto result = routineService.getRoutineById(routineId);

            // Assert
            assertEquals(responseRoutineDto, result);
            verify(routineRepository, times(1)).findById(routineId);
        }

        @Test
        void shouldThrowExceptionWhenRoutineNotFound() {
            // Arrange
            when(routineRepository.findById(routineId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RoutineNotFoundException.class, () -> routineService.getRoutineById(routineId));
        }

        @Test
        void shouldGetRoutinesByUserIdSuccessfully() {
            // Arrange
            List<Routine> routines = List.of(routine);
            when(currentUserService.getCurrentUserId()).thenReturn(userId);
            when(routineRepository.findByUserId(userId)).thenReturn(routines);
            when(routineMapper.toDto(routine)).thenReturn(responseRoutineDto);
            when(currentUserService.getCurrentUserId()).thenReturn(userId);

            // Act
            List<ResponseRoutineDto> result = routineService.getRoutinesByUserId();

            // Assert
            assertEquals(1, result.size());
            assertEquals(responseRoutineDto, result.getFirst());
            verify(routineRepository, times(1)).findByUserId(userId);
        }
    }

    @Nested
    @DisplayName("Get Paged Routine Tests")
    public class GetPagedRoutineTests {

        @Test
        void shouldGetAllPagedRoutinesSuccessfully() {
            // Arrange
            Routine routine = RoutineFactory.createRoutine("good routine", "description");
            Routine routine2 = RoutineFactory.createRoutine("bad routine", "description");
            Routine routine3 = RoutineFactory.createRoutine("bad routine", "description");

            List<Routine> routines = List.of(routine, routine2, routine3);

            Pageable pageable = PageRequest.of(0, 20);

            Page<Routine> mockPage = new PageImpl<>(routines, pageable, routines.size());

            when(routineRepository.findByNameIgnoreCaseContaining("", pageable)).thenReturn(mockPage);

            when(routineMapper.toDto(routine)).thenReturn(responseRoutineDto);
            when(routineMapper.toDto(routine2)).thenReturn(responseRoutineDto);
            when(routineMapper.toDto(routine3)).thenReturn(responseRoutineDto);

            // Act
            RoutineGridDto result = routineService.searchRoutines("", pageable);

            // Assert
            assertEquals(3, result.data().size());
            assertEquals(1, result.totalPages());
        }

        @Test
        void shouldGetSearchedPagedRoutinesSuccessfully() {
            // Arrange
            Routine routine = RoutineFactory.createRoutine("good routine", "description");
            Routine routine2 = RoutineFactory.createRoutine("bad routine", "description");
            Routine routine3 = RoutineFactory.createRoutine("bad routine", "description");

            List<Routine> routines = List.of(routine, routine2, routine3);

            Pageable pageable = PageRequest.of(0, 20);

            List<Routine> searchResults = List.of(routine2, routine3);

            Page<Routine> mockSearchedPage = new PageImpl<>(searchResults, pageable, routines.size());

            when(routineRepository.findByNameIgnoreCaseContaining("bad", pageable)).thenReturn(mockSearchedPage);

            when(routineMapper.toDto(routine2)).thenReturn(responseRoutineDto);
            when(routineMapper.toDto(routine3)).thenReturn(responseRoutineDto);

            // Act
            RoutineGridDto result = routineService.searchRoutines("bad", pageable);

            // Assert
            assertEquals(2, result.data().size());
            assertEquals(1, result.totalPages());
        }

        @Test
        void shouldGetEmptyPagedRoutinesSuccessfully() {

            Pageable pageable = PageRequest.of(0, 20);

            List<Routine> searchResults = List.of();

            Page<Routine> mockSearchedPage = new PageImpl<>(searchResults, pageable, 0);

            when(routineRepository.findByNameIgnoreCaseContaining("", pageable)).thenReturn(mockSearchedPage);
            // Act
            RoutineGridDto result = routineService.searchRoutines("", pageable);

            // Assert
            assertEquals(0, result.data().size());
            assertEquals(0, result.totalPages());
        }
    }

    @Nested
    @DisplayName("Update Routine Tests")
    public class UpdateRoutineTests {

        @Test
        void shouldUpdateRoutineSuccessfully() {
            // Arrange
            when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
            doNothing().when(routineMapper).updateEntity(routine, createRoutineDto);
            when(routineRepository.save(routine)).thenReturn(routine);
            when(routineMapper.toDto(routine)).thenReturn(responseRoutineDto);

            // Act
            ResponseRoutineDto result = routineService.updateRoutine(routineId, createRoutineDto);

            // Assert
            assertEquals(responseRoutineDto, result);
            verify(routineRepository, times(1)).save(routine);
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
            when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));
            doNothing().when(routineRepository).delete(routine);

            // Act
            routineService.deleteRoutine(routineId);

            // Assert
            verify(routineRepository, times(1)).delete(routine);
        }
    }
}
