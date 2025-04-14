package com.sourcery.gymapp.workout.integration.config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class SingletonPostgresContainer {

    private static final PostgreSQLContainer<?> CONTAINER;

    static {
        CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");
        CONTAINER.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return CONTAINER;
    }
}
