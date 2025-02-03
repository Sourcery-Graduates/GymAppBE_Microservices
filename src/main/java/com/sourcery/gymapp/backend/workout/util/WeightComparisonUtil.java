package com.sourcery.gymapp.backend.workout.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class WeightComparisonUtil {

    private record WeightRange(int maxWeight, String message) {}
    private static final List<WeightRange> WEIGHT_RANGES = new ArrayList<>();

    static {
        WEIGHT_RANGES.add(new WeightRange(100, "is like a baby panda! 🐼"));
        WEIGHT_RANGES.add(new WeightRange(400, "is the weight of a newborn giraffe! 🦒"));
        WEIGHT_RANGES.add(new WeightRange(800, "is like a grand piano! 🎹"));
        WEIGHT_RANGES.add(new WeightRange(1200, "is the weight of a motorcycle! 🏍️"));
        WEIGHT_RANGES.add(new WeightRange(2000, "is equivalent to a small car! 🚗"));
        WEIGHT_RANGES.add(new WeightRange(3500, "is like a pickup truck! 🛻"));
        WEIGHT_RANGES.add(new WeightRange(6000, "is almost an adult African elephant! 🐘"));
        WEIGHT_RANGES.add(new WeightRange(9000, "is like a Tyrannosaurus Rex! 🦖"));
        WEIGHT_RANGES.add(new WeightRange(12000, "is like lifting a double-decker bus! 🚌"));
        WEIGHT_RANGES.add(new WeightRange(15000, "is the weight of an M1 Abrams tank! 🚜"));
        WEIGHT_RANGES.add(new WeightRange(25000, "is the tongue of a blue whale. Unreal! 🐋"));
        WEIGHT_RANGES.add(new WeightRange(40000, "is the weight of a humpback whale! 🐳"));
        WEIGHT_RANGES.add(new WeightRange(70000, "is the weight of a space shuttle ready for launch! 🚀"));
        WEIGHT_RANGES.add(new WeightRange(100000, "is like the Statue of Liberty! 🗽"));
        WEIGHT_RANGES.add(new WeightRange(180000, "is like a fully loaded Boeing 747! ✈️"));
        WEIGHT_RANGES.add(new WeightRange(400000, "is approaching the weight of the Eiffel Tower! 🗼"));
        WEIGHT_RANGES.add(new WeightRange(1000000, "is like the mass of the International Space Station! 🛰️"));
        WEIGHT_RANGES.add(new WeightRange(Integer.MAX_VALUE, "is like the mass of an aircraft carrier! Incredible! ⚓"));
    }


    public static String getMessageByWeight(int totalWeightCurrentMonth) {
        for (WeightRange range : WEIGHT_RANGES) {
            if (totalWeightCurrentMonth < range.maxWeight) {
                return range.message;
            }
        }
        return "user moves unlimited weight! 💪";
    }
}
