package com.portal.de.pagamentos.domain.payment_document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyTest {

    @Test
    @DisplayName("Should create Currency with valid code")
    void shouldCreateCurrencyWithValidCode() {
        Currency currency = new Currency("EUR");

        assertEquals("EUR", currency.getValue());
    }

    @Test
    @DisplayName("Should throw exception for invalid code")
    void shouldThrowExceptionForInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> new Currency("eu"));
        assertThrows(IllegalArgumentException.class, () -> new Currency("123"));
        assertThrows(IllegalArgumentException.class, () -> new Currency(null));
    }

    @Test
    @DisplayName("Should set and get valid code")
    void shouldSetAndGetValidCode() {
        Currency currency = new Currency("USD");
        currency.setValue("GBP");

        assertEquals("GBP", currency.getValue());
    }

    @Test
    @DisplayName("toString should return string representation")
    void shouldReturnStringRepresentation() {
        Currency currency = new Currency("JPY");

        assertEquals("JPY", currency.toString());
    }
}
