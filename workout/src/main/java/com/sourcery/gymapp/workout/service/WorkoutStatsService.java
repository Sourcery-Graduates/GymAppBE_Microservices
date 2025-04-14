package com.sourcery.gymapp.workout.service;

import com.sourcery.gymapp.workout.dto.*;
import com.sourcery.gymapp.workout.mapper.RoutineMapper;
import com.sourcery.gymapp.workout.model.Routine;
import com.sourcery.gymapp.workout.repository.WorkoutRepository;
import com.sourcery.gymapp.workout.util.WeightComparisonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkoutStatsService {
    private final WorkoutCurrentUserService workoutCurrentUserService;
    private final OffsetDateService offsetDateService;
    private final WorkoutService workoutService;
    private final RoutineService routineService;
    private final WorkoutRepository workoutRepository;
    private final RoutineMapper routineMapper;

    public List<WorkoutStatsDto> getWorkoutStatsByMonth() {
        UUID userId = workoutCurrentUserService.getCurrentUserId();
        List<WorkoutStatsDto> userStats = new ArrayList<>();

        addWorkoutStats(userStats, userId);
        addWeightStats(userStats, userId);

        return userStats;
    }

    private void addWorkoutStats(List<WorkoutStatsDto> userStats, UUID currentUserId) {
        int totalWorkoutsCurrentMonth = getWorkoutCount(currentUserId, 0);
        int totalWorkoutsPreviousMonth = getWorkoutCount(currentUserId, 1);
        int differenceInWorkouts = totalWorkoutsCurrentMonth - totalWorkoutsPreviousMonth;

        if (totalWorkoutsCurrentMonth >= 1) {
            String isPlural = totalWorkoutsCurrentMonth > 1 ? "s" : "";
            userStats.add(new WorkoutStatsDto(
                    UUID.randomUUID(),
                    "totalWorkouts",
                    "You have completed %d workout%s this month!"
                            .formatted(totalWorkoutsCurrentMonth, isPlural)
            ));
        }

        if (differenceInWorkouts >= 1) {
            String isPlural = totalWorkoutsCurrentMonth > 1 ? "s" : "";
            userStats.add(new WorkoutStatsDto(
                    UUID.randomUUID(),
                    "workoutDifference",
                    "You have completed %d more workout%s than the last month!"
                            .formatted(differenceInWorkouts, isPlural)
            ));
        }
    }

    private void addWeightStats(List<WorkoutStatsDto> userStats, UUID currentUserId) {
        int totalWeightCurrentMonth = getTotalWeight(currentUserId, 0);
        int totalWeightPreviousMonth = getTotalWeight(currentUserId, 1);
        int differenceInWeight = totalWeightCurrentMonth - totalWeightPreviousMonth;

        if (totalWeightCurrentMonth > 0) {
            userStats.add(new WorkoutStatsDto(
                    UUID.randomUUID(),
                    "totalWeight",
                    "You have lifted a total of " + totalWeightCurrentMonth + " kg this month!"
            ));

            userStats.add(new WorkoutStatsDto(
                    UUID.randomUUID(),
                    "totalWeight",
                    totalWeightCurrentMonth +" kg " + WeightComparisonUtil.getMessageByWeight(totalWeightCurrentMonth)
            ));
        }

        if (differenceInWeight > 0) {
            userStats.add(new WorkoutStatsDto(
                    UUID.randomUUID(),
                    "weightDifference",
                    "You have lifted " + differenceInWeight + " kg more than the last month!"
            ));
        }
    }

    private int getWorkoutCount(UUID currentUserId, Integer offsetMonth) {
        List<ZonedDateTime> startAndEndOfTheMonth = offsetDateService.getMonthlyDateRangeOffset(offsetMonth);

        return workoutRepository
                .countWorkoutsByUserIdAndDateBetween(
                        currentUserId,
                        startAndEndOfTheMonth.getFirst(),
                        startAndEndOfTheMonth.getLast()
                );
    }

    private int getTotalWeight(UUID currentUserId, Integer offsetMonth) {
        List<ZonedDateTime> startAndEndOfTheMonth = offsetDateService.getMonthlyDateRangeOffset(offsetMonth);

        Optional<Integer> totalWeight = workoutRepository.getTotalWeightByUserIdAndDateBetween(
                currentUserId,
                startAndEndOfTheMonth.getFirst(),
                startAndEndOfTheMonth.getLast()
        );

        return totalWeight.orElse(0);
    }

    public List<ResponseRoutineSimpleDto> getMostUsedRoutinesByMonth(Integer routinesLimit, Integer offsetStartMonth) {
        int baseRoutinesLimit = 7;
        if (routinesLimit != null) {
            baseRoutinesLimit = routinesLimit;
        }
        if (baseRoutinesLimit < 1) {
            throw new IllegalArgumentException("Routines limit must be greater than 0");
        }

        UUID currentUserId = workoutCurrentUserService.getCurrentUserId();

        List<ZonedDateTime> startAndEndOfTheMonth = offsetDateService.getOffsetStartAndCurrentDate(offsetStartMonth);

        List<Routine> routines = workoutRepository.getMostUsedRoutinesByUserIdAndDateBetween(
                currentUserId,
                startAndEndOfTheMonth.getFirst(),
                startAndEndOfTheMonth.getLast()
        );

        return routines.stream()
                .map(routineMapper::toSimpleDto)
                .limit(baseRoutinesLimit)
                .toList();
    }

    public List<MuscleSetDto> getTotalMuscleSetsByWeek(Integer offsetWeek) {
        UUID currentUserId = workoutCurrentUserService.getCurrentUserId();
        List<ZonedDateTime> startAndEndOfTheWeek = offsetDateService.getWeeklyDateRangeOffset(offsetWeek);

        return workoutRepository.getTotalMuscleSetsByUserIdAndDateBetween(
                currentUserId,
                startAndEndOfTheWeek.getFirst(),
                startAndEndOfTheWeek.getLast()
        );
    }

    public boolean checkIfUserIsNew() {
        List<ResponseWorkoutDto> workouts = workoutService.getWorkoutsByUserId();
        List<ResponseRoutineDto> routines = routineService.getRoutinesByUserId();

        return workouts.isEmpty() && routines.isEmpty();
    }
}
