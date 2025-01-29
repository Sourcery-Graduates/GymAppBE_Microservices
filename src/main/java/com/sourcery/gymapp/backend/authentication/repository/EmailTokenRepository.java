package com.sourcery.gymapp.backend.authentication.repository;

import com.sourcery.gymapp.backend.authentication.model.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailTokenRepository extends JpaRepository<EmailToken, UUID> {

    Optional<EmailToken> findByToken(String token);
}
