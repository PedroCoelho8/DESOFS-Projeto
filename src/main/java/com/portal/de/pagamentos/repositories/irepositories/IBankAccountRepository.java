package com.portal.de.pagamentos.repositories.irepositories;

import com.portal.de.pagamentos.domain.bank_account.BankAccount;
import com.portal.de.pagamentos.domain.bank_account.IBAN;
import com.portal.de.pagamentos.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IBankAccountRepository extends JpaRepository<BankAccount, Integer> {

    Optional<BankAccount> findByIban_Value(String iban);

    Optional<BankAccount> findByNifUser_Value(String nifUser);

    List<BankAccount> findByCreatedBy(User user);

    @Query("SELECT b.iban FROM BankAccount b WHERE b.createdBy = :user")
    List<IBAN> findIbanByCreatedBy(@Param("user") User user);

    boolean existsByIban_Value(String iban);

    boolean existsByNifUser_Value(String nifUser);

}
