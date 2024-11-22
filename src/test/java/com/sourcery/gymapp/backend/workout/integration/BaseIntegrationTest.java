package com.sourcery.gymapp.backend.workout.integration;

import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Tag("integration")
@Testcontainers
@AutoConfigureMockMvc
@WithMockUser(username = "test_user")
@Transactional
public abstract class BaseIntegrationTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14-alpine")
                    .withCopyFileToContainer(
                            MountableFile.forHostPath("./src/main/resources/db/changelog/changes/workout_data/exercises_data/formatted_exercises.json"),
                            "/var/lib/postgresql/import-data/formatted_exercises.json"
                    );

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Test
    void connectionEstablished() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @BeforeAll
    public static void initializeDatabase() {
        try (var connection = postgreSQLContainer.createConnection("")) {
            var statement = connection.createStatement();
            statement.executeUpdate("CREATE ROLE gym_app_user WITH LOGIN PASSWORD 'password';");
            statement.executeUpdate("GRANT ALL PRIVILEGES ON DATABASE test TO gym_app_user;");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }


    @BeforeEach
    public void setUp() {
        // for setup
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("DO $$ DECLARE r RECORD; BEGIN FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'workout_data') LOOP EXECUTE 'TRUNCATE TABLE workout_data.' || r.tablename || ' CASCADE'; END LOOP; END $$;");
    }
}
