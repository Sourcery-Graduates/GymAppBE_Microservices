package com.sourcery.gymapp.backend.workout.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sourcery.gymapp.backend.workout.integration.config.SingletonPostgresContainer;
import com.sourcery.gymapp.backend.workout.integration.config.TestAuditConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * BaseIntegrationTest serves as a base class for integration tests utilizing a shared PostgreSQLContainer.
 * <p>
 * Note: During the shutdown process of the Spring application context, there may be warnings or errors
 * in the logs related to broken connections (e.g., "HikariPool-1 - Connection marked as broken" or
 * "Unable to rollback against JDBC Connection").
 * <p>
 * These issues occur because the PostgreSQL container may shut down before Hibernate or Spring
 * finishes cleaning up transactions or releasing resources. This is a known behavior with Testcontainers
 * and the HikariCP connection pool during shutdown.
 * <p>
 * However, this does not impact the correctness or success of the tests, as these warnings only occur
 * during the teardown phase. They can safely be ignored unless they explicitly cause test failures.
 */
@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Import(TestAuditConfig.class)
@ActiveProfiles("test")
@Tag("integration")
@Testcontainers
public abstract class BaseWorkoutIntegrationJPATest {
    private static final PostgreSQLContainer<?> postgresContainer = SingletonPostgresContainer.getInstance();

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Test
    void connectionEstablished() {
        assertThat(postgresContainer.isCreated()).isTrue();
        assertThat(postgresContainer.isRunning()).isTrue();
    }

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
