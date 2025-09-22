package com.portal.de.pagamentos.domain.payment_document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentValueTest {

    @Test
    @DisplayName("Should create PaymentValue with correct rounding")
    void shouldCreatePaymentValueCorrectly() {
        PaymentValue paymentValue = new PaymentValue(123.456);

        assertEquals(new BigDecimal("123.46"), paymentValue.getValue());
    }

    @Test
    @DisplayName("Should throw exception for negative value")
    void shouldThrowExceptionForNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> new PaymentValue(-50.0));
    }

    @Test
    @DisplayName("Should set and get value with rounding")
    void shouldSetAndGetValue() {
        PaymentValue paymentValue = new PaymentValue(0.0);
        paymentValue.setValue(99.999);

        assertEquals(new BigDecimal("100.00"), paymentValue.getValue());
    }

    @Test
    @DisplayName("toString should return string representation")
    void shouldReturnStringRepresentation() {
        PaymentValue paymentValue = new PaymentValue(55.5);

        assertEquals("55.50", paymentValue.toString());
    }
}
