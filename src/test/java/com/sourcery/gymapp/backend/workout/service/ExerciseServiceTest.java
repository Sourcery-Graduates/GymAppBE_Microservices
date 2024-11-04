package com.sourcery.gymapp.backend.workout.service;

import com.sourcery.gymapp.backend.workout.dto.ExerciseDetailDto;
import com.sourcery.gymapp.backend.workout.dto.ExercisePageDto;
import com.sourcery.gymapp.backend.workout.exception.ExerciseNotFoundException;
import com.sourcery.gymapp.backend.workout.factory.ExerciseFactory;
import com.sourcery.gymapp.backend.workout.mapper.ExerciseMapper;
import com.sourcery.gymapp.backend.workout.model.Exercise;
import com.sourcery.gymapp.backend.workout.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    @Mock
    private ExerciseMapper exerciseMapper;

    private Exercise exercise1;
    private Exercise exercise2;
    private List<UUID> exerciseIds;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        exercise1 = ExerciseFactory.createExercise();
        exercise2 = ExerciseFactory.createExercise();
        exerciseIds = List.of(exercise1.getId(), exercise2.getId());
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void shouldReturnExerciseMapWhenAllExercisesAreFound() {
        when(exerciseRepository.findAllByIdIn(anyList())).thenReturn(List.of(exercise1, exercise2));

        Map<UUID, Exercise> result = exerciseService.getExerciseMapByIds(exerciseIds);

        assertEquals(2, result.size());
        assertEquals(exercise1, result.get(exercise1.getId()));
        assertEquals(exercise2, result.get(exercise2.getId()));
    }

    @Test
    void shouldThrowExerciseNotFoundExceptionWhenSomeExercisesAreMissing() {
        when(exerciseRepository.findAllByIdIn(anyList())).thenReturn(List.of(exercise1));

        ExerciseNotFoundException exception = assertThrows(
                ExerciseNotFoundException.class,
                () -> exerciseService.getExerciseMapByIds(exerciseIds)
        );

        String expectedMessage = "Can't find Exercises by IDs [" + exercise2.getId() + "]";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Nested
    @DisplayName("Get Paged Exercises Tests")
    public class GetPagedExercisesTests {

        private ExerciseDetailDto exerciseDetailDto1;
        private ExerciseDetailDto exerciseDetailDto2;

        @BeforeEach
        void setup() {
            exerciseDetailDto1 = ExerciseFactory.createExerciseDetailDto();
            exerciseDetailDto2 = ExerciseFactory.createExerciseDetailDto();
        }

        @Test
        void shouldGetAllPagedExercisesSuccessfully() {
            List<Exercise> exercises = List.of(exercise1, exercise2);
            Page<Exercise> mockPage = new PageImpl<>(exercises, pageable, exercises.size());
            List<ExerciseDetailDto> responseExercisesDto = List.of(exerciseDetailDto1, exerciseDetailDto2);

            when(exerciseRepository.findByPrefixOrContaining(anyString(), any(Pageable.class))).thenReturn(mockPage);
            when(exerciseMapper.toDto(exercise1)).thenReturn(exerciseDetailDto1);
            when(exerciseMapper.toDto(exercise2)).thenReturn(exerciseDetailDto2);

            ExercisePageDto result = exerciseService.getExercisesByPrefix("Test", pageable);

            assertAll(
                    () -> assertEquals(1, result.totalPages()),
                    () -> assertEquals(responseExercisesDto.size(), result.totalElements()),
                    () -> assertEquals(responseExercisesDto, result.data())
            );
        }

        @Test
        void shouldGetEmptyPagedExercisesSuccessfully() {
            Page<Exercise> mockSearchedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(exerciseRepository.findByPrefixOrContaining(anyString(), any(Pageable.class))).thenReturn(mockSearchedPage);

            ExercisePageDto result = exerciseService.getExercisesByPrefix("Test", pageable);

            assertAll(
                    () -> assertEquals(0, result.totalPages()),
                    () -> assertEquals(0, result.totalElements()),
                    () -> assertTrue(result.data().isEmpty())
            );
        }

        @Test
        void shouldGetFilteredPagedExercisesSuccessfully() {
            String searchPrefix = "Test";
            List<Exercise> filteredExercises = List.of(exercise1);
            Page<Exercise> mockFilteredPage = new PageImpl<>(filteredExercises, pageable, filteredExercises.size());

            when(exerciseRepository.findByPrefixOrContaining(searchPrefix, pageable)).thenReturn(mockFilteredPage);
            when(exerciseMapper.toDto(exercise1)).thenReturn(exerciseDetailDto1);

            ExercisePageDto result = exerciseService.getExercisesByPrefix(searchPrefix, pageable);

            assertAll(
                    () -> assertEquals(1, result.totalPages()),
                    () -> assertEquals(1, result.totalElements()),
                    () -> assertEquals(List.of(exerciseDetailDto1), result.data())
            );
        }
    }
}

