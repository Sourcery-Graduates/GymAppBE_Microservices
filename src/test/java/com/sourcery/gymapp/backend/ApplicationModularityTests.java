package com.sourcery.gymapp.backend;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ApplicationModularityTests {
    static ApplicationModules modules = ApplicationModules.of(GymAppBackendApplication.class);

    @Test
    void bootstrapsApplicationModules() {
        modules.verify();
    }

}
