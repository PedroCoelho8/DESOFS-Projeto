package com.portal.de.pagamentos.domain.payment;

import com.portal.de.pagamentos.domain.bank_account.BankAccount;
import com.portal.de.pagamentos.domain.payment_document.PaymentDocument;
import com.portal.de.pagamentos.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PaymentTest {

    private Payment payment;
    private BankAccount senderAccount;
    private BankAccount receiverAccount;
    private User creator;
    private User updater;
    private PaymentDocument paymentDocument;

    @BeforeEach
    void setup() {
        senderAccount = mock(BankAccount.class);
        receiverAccount = mock(BankAccount.class);
        creator = mock(User.class);
        updater = mock(User.class);
        paymentDocument = mock(PaymentDocument.class);

        payment = new Payment();
        payment.setSenderBankAccount(senderAccount);
        payment.setReceiverBankAccount(receiverAccount);
        payment.setCreatedBy(creator);
        payment.setState(PaymentState.PENDING);
    }

    @Test
    @DisplayName("Should create Payment with correct initial values")
    void shouldCreatePaymentCorrectly() {
        assertNull(payment.getId());
        assertEquals(senderAccount, payment.getSenderBankAccount());
        assertEquals(receiverAccount, payment.getReceiverBankAccount());
        assertEquals(creator, payment.getCreatedBy());
        assertEquals(PaymentState.PENDING, payment.getState());
        assertNull(payment.getUpdatedBy());
        assertNull(payment.getDtCreated());
        assertNull(payment.getDtUpdated());
        assertTrue(payment.getPaymentDocuments().isEmpty());
    }

    @Test
    @DisplayName("Should update dtUpdated on update")
    void shouldUpdateDtUpdated() {
        payment.onUpdate();

        assertNotNull(payment.getDtUpdated());
        LocalDateTime updated = payment.getDtUpdated();
        assertTrue(updated.isBefore(LocalDateTime.now()) || updated.isEqual(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should set and get updatedBy")
    void shouldSetAndGetUpdatedBy() {
        payment.setUpdatedBy(updater);
        assertEquals(updater, payment.getUpdatedBy());
    }

    @Test
    @DisplayName("Should add payment documents and amounts")
    void shouldAddPaymentDocuments() {
        Map<PaymentDocument, Double> docs = new HashMap<>();
        docs.put(paymentDocument, 100.0);

        payment.setPaymentDocuments(docs);

        assertEquals(1, payment.getPaymentDocuments().size());
        assertTrue(payment.getPaymentDocuments().containsKey(paymentDocument));
        assertEquals(100.0, payment.getPaymentDocuments().get(paymentDocument));
    }

    @Test
    @DisplayName("Should change state")
    void shouldChangeState() {
        payment.setState(PaymentState.PAYED);
        assertEquals(PaymentState.PAYED, payment.getState());
    }
}
