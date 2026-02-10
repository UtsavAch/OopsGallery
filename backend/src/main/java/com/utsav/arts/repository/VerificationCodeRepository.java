package com.utsav.arts.repository;

import com.utsav.arts.models.VerificationCode;
import com.utsav.arts.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing {@link VerificationCode} entities.
 *
 * <p>
 * Extends {@link JpaRepository} to leverage Spring Data JPA's
 * built-in CRUD operations, while also defining custom queries
 * related to user account verification.
 * </p>
 */
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {

    /**
     * Finds a verification code associated with a specific user.
     *
     * <p>
     * Typically used during account verification or resend-code flows.
     * </p>
     *
     * @param user the user whose verification code is being searched
     * @return an {@link Optional} containing the verification code if found
     */
    Optional<VerificationCode> findByUser(User user);

    /**
     * Finds a verification code by its code value.
     *
     * <p>
     * Used when a user submits a verification code for validation.
     * </p>
     *
     * @param code the verification code string
     * @return an {@link Optional} containing the verification code if found
     */
    Optional<VerificationCode> findByCode(String code);

    /**
     * Finds verification codes that are expired and belong to users
     * who have not verified their accounts.
     *
     * <p>
     * This query is useful for scheduled cleanup tasks that remove
     * stale verification codes and potentially unverified user accounts.
     * </p>
     *
     * @param now     the current timestamp used to check expiration
     * @param cutoff a timestamp representing the minimum account age
     *               before considering cleanup
     * @return a list of expired verification codes linked to unverified users
     */
    @Query("""
SELECT v FROM VerificationCode v
WHERE v.expiryDate < :now
AND v.user.createdAt < :cutoff
AND v.user.enabled = false
""")
    List<VerificationCode> findExpiredUnverified(
            @Param("now") LocalDateTime now,
            @Param("cutoff") LocalDateTime cutoff
    );
}