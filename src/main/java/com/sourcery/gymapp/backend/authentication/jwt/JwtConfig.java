package com.sourcery.gymapp.backend.authentication.jwt;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.public-key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.private-key}")
    private RSAPrivateKey privateKey;

    @Bean
    public JwtEncoder jwtEncoder() {
        // Create a JWK for signing, specifying KeyUse as SIGNATURE
        JWK jwk = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyUse(com.nimbusds.jose.jwk.KeyUse.SIGNATURE) // Ensuring the key is used for signing
                .build();

        // Create a JWKSource from the JWK
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Configure the JWT decoder with the public key to verify the token's signature
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
