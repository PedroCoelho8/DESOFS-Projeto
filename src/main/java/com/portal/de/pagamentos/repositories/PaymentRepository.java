package com.portal.de.pagamentos.repositories;

import com.portal.de.pagamentos.domain.payment.Payment;
import com.portal.de.pagamentos.domain.payment.PaymentState;
import com.portal.de.pagamentos.repositories.irepositories.IPaymentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PaymentRepository
        extends SimpleJpaRepository<Payment, UUID>
        implements IPaymentRepository {

    public PaymentRepository(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }

    @Override
    public List<Payment> findByState(String state) {
        PaymentState stateEnum = PaymentState.valueOf(state);
        return findAll().stream()
                .filter(p -> p.getState() == stateEnum)
                .toList();
    }

    @Override
    public List<Payment> findBySenderBankAccountCreatedById(UUID userId) {
        return findAll().stream()
                .filter(p -> p.getSenderBankAccount() != null
                        && p.getSenderBankAccount().getCreatedBy() != null
                        && p.getSenderBankAccount().getCreatedBy().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Payment> findByReceiverBankAccountCreatedById(UUID userId) {
        return findAll().stream()
                .filter(p -> p.getReceiverBankAccount() != null
                        && p.getReceiverBankAccount().getCreatedBy() != null
                        && p.getReceiverBankAccount().getCreatedBy().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Payment> findByCreatedById(UUID userId) {
        return findAll().stream()
                .filter(p -> p.getCreatedBy() != null
                        && p.getCreatedBy().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Payment> findPaymentsByUser(UUID userId) {
        return findAll().stream()
                .filter(p ->
                        (p.getSenderBankAccount() != null &&
                                p.getSenderBankAccount().getCreatedBy() != null &&
                                p.getSenderBankAccount().getCreatedBy().getId().equals(userId))
                                ||
                                (p.getReceiverBankAccount() != null &&
                                        p.getReceiverBankAccount().getCreatedBy() != null &&
                                        p.getReceiverBankAccount().getCreatedBy().getId().equals(userId))
                )
                .toList();
    }
}
