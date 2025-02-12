package com.sourcery.gymapp.backend.config.integration;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.kafka.ConfluentKafkaContainer;

public class BaseKafkaIntegrationTest extends BaseIntegrationTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Container
    static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("""
                    DO $$ 
                    DECLARE r RECORD;
                    BEGIN 
                        FOR r IN (SELECT schemaname, tablename FROM pg_catalog.pg_tables WHERE schemaname IN ('user_profiles', 'user_auth'))
                        LOOP 
                            EXECUTE 'TRUNCATE TABLE ' || r.schemaname || '.' || r.tablename || ' CASCADE'; 
                        END LOOP; 
                    END $$;
                """);
    }
}
