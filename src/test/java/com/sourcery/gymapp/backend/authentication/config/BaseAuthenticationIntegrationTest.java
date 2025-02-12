package com.sourcery.gymapp.backend.authentication.config;

import com.sourcery.gymapp.backend.config.integration.BaseIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseAuthenticationIntegrationTest extends BaseIntegrationTest {


    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("""
            DO $$ DECLARE r RECORD;
            BEGIN 
                FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'user_auth')
                LOOP 
                    EXECUTE 'TRUNCATE TABLE user_auth.' || r.tablename || ' CASCADE'; 
                END LOOP; 
            END $$;
        """);
    }
}
