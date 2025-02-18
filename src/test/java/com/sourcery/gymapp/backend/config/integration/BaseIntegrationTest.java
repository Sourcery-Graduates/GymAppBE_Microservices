package com.sourcery.gymapp.backend.config.integration;

import com.sourcery.gymapp.backend.authentication.jwt.JwtConfig;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;


@Testcontainers
@ActiveProfiles("test")
@Tag("integration")
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest implements BaseTestTeardownLifecycle {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected WebTestClient webTestClient;

    protected static String jwtToken;
    protected static String username = "testUser";
    protected static String userId = "00000000-0000-0000-0000-000000000001";
    protected static String email = "testUser@user.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }
    @BeforeAll
    public static void setup(@Autowired JwtConfig jwtConfig, @Autowired JdbcTemplate jdbcTemplate) {

    jdbcTemplate.execute("""
                    DO $$ 
                    DECLARE r RECORD;
                    BEGIN 
                        FOR r IN (SELECT schemaname, tablename FROM pg_catalog.pg_tables WHERE schemaname IN ('user_profiles', 'user_auth', 'shared_links', 'workout_data'))
                        LOOP 
                            EXECUTE 'TRUNCATE TABLE ' || r.schemaname || '.' || r.tablename || ' CASCADE'; 
                        END LOOP; 
                    END $$;
                """);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .subject(username)
                .issuedAt(Instant.ofEpochSecond(1733490155))
                .claim("userId", userId)
                .claim("username", username)
                .claim("email", email)
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);
        jwtToken = jwtConfig.jwtEncoder().encode(parameters).getTokenValue();
    }
}
