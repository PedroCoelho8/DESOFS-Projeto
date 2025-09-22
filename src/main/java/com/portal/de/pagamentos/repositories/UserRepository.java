package com.portal.de.pagamentos.repositories;

import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.repositories.irepositories.IUserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository extends SimpleJpaRepository<User, UUID> implements IUserRepository {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.findAll().stream()
                .filter(user -> user.getEmail().getValue().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByNif(String nif) {
        return this.findAll().stream()
                .filter(user -> user.getNif().equals(nif))
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public boolean existsByNif(String nif) { return findByNif(nif).isPresent(); }

    @Override
    @Transactional
    public User insert(User user) {
        if (user.getId() != null || existsById(user.getId())) {
            throw new IllegalArgumentException("Cannot create a user that already exists");
        }

        return this.save(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        if (user.getId() == null || !existsById(user.getId())) {
            throw new IllegalArgumentException("Cannot update a user that does not exist");
        }
        return this.save(user);
    }

}