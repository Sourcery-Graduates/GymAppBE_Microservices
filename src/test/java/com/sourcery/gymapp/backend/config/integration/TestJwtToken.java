package com.sourcery.gymapp.backend.config.integration;

import com.sourcery.gymapp.backend.authentication.jwt.JwtConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestJwtToken extends BaseIntegrationTest {

    @Autowired
    private JwtConfig jwtConfig;

    public void tearDown() {
        // not needed in this class
    }
    @Test
    public void givenCreatedTestJwt_shouldBeValid() {
        assertNotNull(jwtToken);

        Jwt decodedJwt = jwtConfig.jwtDecoder().decode(jwtToken);
        assertEquals(decodedJwt.getSubject(), username);
        assertEquals(decodedJwt.getClaim("userId"), userId);
        assertEquals(decodedJwt.getClaim("username"), username);
        assertEquals(decodedJwt.getClaim("email"), email);
    }
}
