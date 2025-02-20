package com.sourcery.gymapp.backend.authentication.repository;

import com.sourcery.gymapp.backend.authentication.model.EmailToken;
import com.sourcery.gymapp.backend.authentication.model.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface EmailTokenRepository extends JpaRepository<EmailToken, UUID> {

    Optional<EmailToken> findByToken(String token);

    /**
     * Retrieves the {@link EmailToken} associated with the specified token and locks the row
     * immediately for update. If another transaction tries to lock the same row, it will fail
     * instantly without waiting, throwing error.
     *
     * <p>Throws a {@link CannotAcquireLockException} if the row is already locked by another
     * transaction, and it cannot acquire the lock.</p>
     *
     * @param token the token associated with the email token to retrieve.
     * @return an {@link Optional} containing the {@link EmailToken} if found, or {@link Optional#empty()}
     *         if no token is found with the provided token.
     * @throws CannotAcquireLockException if the row is already locked by another transaction.
     */
    @Query(value = "SELECT * FROM user_auth.email_token WHERE token = :token FOR UPDATE NOWAIT", nativeQuery = true)
    Optional<EmailToken> findByTokenAndLockRowAccess(@Param("token") String token);

    List<EmailToken> findAllByUserIdAndType(UUID userId, TokenType type);
}
