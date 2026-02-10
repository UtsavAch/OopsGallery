package com.utsav.arts.repository;

import com.utsav.arts.models.VerificationCode;
import com.utsav.arts.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    Optional<VerificationCode> findByUser(User user);
    Optional<VerificationCode> findByCode(String code);

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