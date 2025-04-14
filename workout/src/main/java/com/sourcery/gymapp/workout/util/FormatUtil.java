package com.sourcery.gymapp.workout.util;

public class FormatUtil {

    /**
     * @param string string to wrap in curly braces, it is needed for jpa query due to casting
     * @return string wrapped in curly braces
     */
    public static String wrapInCurlyBraces(String string) {
        return "{" + string + "}";
    }
}
