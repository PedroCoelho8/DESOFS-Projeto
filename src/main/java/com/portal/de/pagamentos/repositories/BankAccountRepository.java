package com.portal.de.pagamentos.repositories;

import com.portal.de.pagamentos.domain.bank_account.BankAccount;
import com.portal.de.pagamentos.domain.bank_account.IBAN;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.repositories.irepositories.IBankAccountRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class BankAccountRepository
        extends SimpleJpaRepository<BankAccount, Integer>
        implements IBankAccountRepository {

    public BankAccountRepository(EntityManager entityManager) {
        super(BankAccount.class, entityManager);
    }

    @Override
    public Optional<BankAccount> findByIban_Value(String iban) {
        return this.findAll().stream()
                .filter(b -> b.getIban().getValue().equals(iban))
                .findFirst();
    }

    @Override
    public Optional<BankAccount> findByNifUser_Value(String nifUser) {
        return this.findAll().stream()
                .filter(b -> b.getNifUser().getValue().equals(nifUser))
                .findFirst();
    }

    @Override
    public List<BankAccount> findByCreatedBy(User user) {
        return this.findAll().stream()
                .filter(b -> b.getCreatedBy().equals(user))
                .toList();
    }

    @Override
    public List<IBAN> findIbanByCreatedBy(User user) {
        throw new UnsupportedOperationException("This method is implemented by @Query");
    }

    @Override
    public boolean existsByIban_Value(String iban) {
        return findByIban_Value(iban).isPresent();
    }

    @Override
    public boolean existsByNifUser_Value(String nifUser) {
        return findByNifUser_Value(nifUser).isPresent();
    }

    @Override
    @Transactional
    public <S extends BankAccount> S save(S entity) {
        return super.save(entity);
    }
}
