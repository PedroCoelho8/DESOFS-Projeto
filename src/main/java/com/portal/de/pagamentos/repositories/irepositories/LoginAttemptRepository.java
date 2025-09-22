package com.portal.de.pagamentos.repositories.irepositories;

import com.portal.de.pagamentos.domain.login.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    // Find by email instead of using email as ID
    Optional<LoginAttempt> findByEmail(String email);

    // Delete by email instead of using email as ID
    @Modifying
    @Transactional
    void deleteByEmail(String email);

    // Check if exists by email
    boolean existsByEmail(String email);
}