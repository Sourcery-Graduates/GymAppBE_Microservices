package com.sourcery.gymapp.workout;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest("spring.liquibase.enabled=false")
@ActiveProfiles("test")
class WorkoutApplicationTests {

    @Test
    void contextLoads() {
    }

}
