package com.portal.de.pagamentos.repositories.irepositories;

import com.portal.de.pagamentos.domain.login.TwoFactorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ITwoFactorRepository extends JpaRepository<TwoFactorCode, Long> {

    // Find by email and not used
    Optional<TwoFactorCode> findByEmailAndUsedFalse(String email);

    // Delete by email instead of using email as ID
    @Modifying
    @Transactional
    void deleteByEmail(String email);

    // Delete expired codes
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM TwoFactorCode t WHERE t.expiresAt < :cutoff")
    void deleteByExpiresAtBefore(@Param("cutoff") LocalDateTime cutoff);

    // Find by email (useful for cleanup)
    Optional<TwoFactorCode> findByEmail(String email);
}