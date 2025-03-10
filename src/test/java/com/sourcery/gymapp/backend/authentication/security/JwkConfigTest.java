package com.sourcery.gymapp.backend.authentication.security;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.sourcery.gymapp.backend.authentication.config.JwkConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import static org.junit.jupiter.api.Assertions.*;

class JwkConfigTest {

    private final JwkConfig jwkConfig = new JwkConfig();

    @Test
    void jwkSourceShouldNotBeNull() {
        JWKSource<SecurityContext> jwkSource = jwkConfig.jwkSource();
        assertNotNull(jwkSource);
    }

    @Test
    void jwtDecoderShouldNotBeNull() {
        JwtDecoder jwtDecoder = jwkConfig.jwtDecoder();
        assertNotNull(jwtDecoder);
    }

    @Test
    void tokenGeneratorShouldNotBeNull() {
        OAuth2TokenGenerator<?> tokenGenerator = jwkConfig.tokenGenerator();
        assertNotNull(tokenGenerator);
    }
}
