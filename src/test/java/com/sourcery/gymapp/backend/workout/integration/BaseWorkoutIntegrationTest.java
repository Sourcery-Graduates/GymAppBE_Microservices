package com.sourcery.gymapp.backend.workout.integration;

import com.sourcery.gymapp.backend.config.integration.BaseIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;



@ActiveProfiles("test")
@Tag("integration")
public abstract class BaseWorkoutIntegrationTest extends BaseIntegrationTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("""
            DO $$ DECLARE r RECORD;
            BEGIN 
                FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'workout_data')
                LOOP 
                    EXECUTE 'TRUNCATE TABLE workout_data.' || r.tablename || ' CASCADE'; 
                END LOOP; 
            END $$;
        """);
    }
}
