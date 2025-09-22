package com.portal.de.pagamentos.repositories.irepositories;

import com.portal.de.pagamentos.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;


@NoRepositoryBean
public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNif(String nif);

    boolean existsByEmail(String email);

    boolean existsByNif(String nif);

    User insert(User user);

    User update(User user);
}