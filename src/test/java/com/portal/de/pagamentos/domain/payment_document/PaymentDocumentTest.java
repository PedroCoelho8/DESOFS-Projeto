package com.portal.de.pagamentos.domain.payment_document;

import com.portal.de.pagamentos.domain.NIF;
import com.portal.de.pagamentos.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PaymentDocumentTest {

    private PaymentDocument paymentDocument;
    private NIF nifSender;
    private NIF nifReceiver;
    private User creator;
    private User updater;

    @BeforeEach
    void setup() {
        nifSender = new NIF("123456789"); // substitua pelo seu valor
        nifReceiver = new NIF("245618457"); // substitua pelo seu valor
        creator = mock(User.class);
        updater = mock(User.class);
        paymentDocument = new PaymentDocument(nifSender, nifReceiver, 250.0, "EUR", creator);
    }

    @Test
    @DisplayName("Should create PaymentDocument with correct initial values")
    void shouldCreatePaymentDocumentCorrectly() {
        assertNull(paymentDocument.getId());
        assertEquals(nifSender, paymentDocument.getNifSender());
        assertEquals(nifReceiver, paymentDocument.getNifReceiver());
        assertEquals(250.0, paymentDocument.getValue());
        assertEquals("EUR", paymentDocument.getCurrency());
        assertEquals(creator, paymentDocument.getCreatedBy());
        assertNull(paymentDocument.getUpdatedBy());
        assertNull(paymentDocument.getDtCreate());
        assertNull(paymentDocument.getDtUpdated());
    }

    @Test
    @DisplayName("Should update dtUpdated on update")
    void shouldUpdateDtUpdated() {
        paymentDocument.onUpdate();

        assertNotNull(paymentDocument.getDtUpdated());
        LocalDateTime updated = paymentDocument.getDtUpdated();
        assertTrue(updated.isBefore(LocalDateTime.now()) || updated.isEqual(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should set and get updatedBy")
    void shouldSetAndGetUpdatedBy() {
        paymentDocument.setUpdatedBy(updater);
        assertEquals(updater, paymentDocument.getUpdatedBy());
    }

    @Test
    @DisplayName("Should set NIF sender and receiver")
    void shouldSetNifSenderAndReceiver() {
        paymentDocument.setNifSender("123456789");
        paymentDocument.setNifReceiver("245618457");

        assertEquals("123456789", paymentDocument.getNifSender().getValue());
        assertEquals("245618457", paymentDocument.getNifReceiver().getValue());
    }

    @Test
    @DisplayName("Should set value and currency")
    void shouldSetValueAndCurrency() {
        paymentDocument.setValue(500.0);
        paymentDocument.setCurrency("USD");

        assertEquals(500.0, paymentDocument.getValue());
        assertEquals("USD", paymentDocument.getCurrency());
    }
}
