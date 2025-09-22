package com.portal.de.pagamentos.domain.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStateTest {

    @Test
    @DisplayName("Should find PaymentState by valid code")
    void shouldFindByValidCode() {
        assertTrue(PaymentState.findByCode("PENDING").isPresent());
        assertEquals(PaymentState.PENDING, PaymentState.findByCode("PENDING").get());

        assertTrue(PaymentState.findByCode("payed").isPresent());
        assertEquals(PaymentState.PAYED, PaymentState.findByCode("payed").get());
    }

    @Test
    @DisplayName("Should return empty Optional for invalid code")
    void shouldReturnEmptyForInvalidCode() {
        assertTrue(PaymentState.findByCode("INVALID").isEmpty());
        assertTrue(PaymentState.findByCode(null).isEmpty());
    }

    @Test
    @DisplayName("Should get PaymentState by valid code")
    void shouldGetByValidCode() {
        assertEquals(PaymentState.CANCELED, PaymentState.getByCode("CANCELED"));
    }

    @Test
    @DisplayName("Should throw exception for invalid code in getByCode")
    void shouldThrowForInvalidCodeInGetByCode() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PaymentState.getByCode("INVALID")
        );
        assertTrue(exception.getMessage().contains("Nenhum estado de pagamento encontrado"));
    }

    @Test
    @DisplayName("Should identify final states correctly")
    void shouldIdentifyFinalStates() {
        assertTrue(PaymentState.PAYED.isFinalState());
        assertTrue(PaymentState.CANCELED.isFinalState());
        assertFalse(PaymentState.PENDING.isFinalState());
        assertFalse(PaymentState.CONFIRMED_TO_BE_PAID.isFinalState());
    }

    @Test
    @DisplayName("Should identify cancelable states correctly")
    void shouldIdentifyCancelableStates() {
        assertTrue(PaymentState.PENDING.isCancelable());
        assertTrue(PaymentState.CONFIRMED_TO_BE_PAID.isCancelable());
        assertFalse(PaymentState.PAYED.isCancelable());
        assertFalse(PaymentState.CANCELED.isCancelable());
    }

    @Test
    @DisplayName("Should return correct code and description")
    void shouldReturnCorrectCodeAndDescription() {
        assertEquals("PENDING", PaymentState.PENDING.getCode());
        assertEquals("Pendente", PaymentState.PENDING.getDescription());
    }
}
