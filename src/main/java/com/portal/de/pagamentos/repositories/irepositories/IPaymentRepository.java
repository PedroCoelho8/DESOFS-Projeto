package com.portal.de.pagamentos.repositories.irepositories;

import com.portal.de.pagamentos.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IPaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByState(String state);

    List<Payment> findBySenderBankAccountCreatedById(UUID userId);

    List<Payment> findByReceiverBankAccountCreatedById(UUID userId);

    List<Payment> findByCreatedById(UUID userId);

    default List<Payment> findPaymentsByUser(UUID userId) {
        throw new UnsupportedOperationException("Custom implementation in concrete class only");
    }
}
