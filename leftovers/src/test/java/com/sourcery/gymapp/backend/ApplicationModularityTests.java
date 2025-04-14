package com.sourcery.gymapp.

import com.sourcery.gymapp.backend.workout.WorkoutApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ApplicationModularityTests {
    static ApplicationModules modules = ApplicationModules.of(WorkoutApplication.class);

    @Test
    void bootstrapsApplicationModules() {
        modules.verify();
    }

}
