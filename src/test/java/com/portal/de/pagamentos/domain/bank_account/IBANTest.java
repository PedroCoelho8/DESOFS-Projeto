package com.portal.de.pagamentos.domain.bank_account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IBANTest {

    private static final String FORMAT_ERROR = "IBAN inválido. Deve seguir o formato internacional (ex: PT50123456789012345678901)";
    private static final String LENGTH_ERROR = "IBAN inválido. Deve ter entre 15 e 34 caracteres.";

    @Test
    @DisplayName("Should create IBAN with valid value")
    void shouldCreateIBANWithValidValue() {
        String validIBAN = "PT50123456789012345678901";

        IBAN iban = new IBAN(validIBAN);

        assertEquals(validIBAN, iban.getValue());
        assertEquals(validIBAN, iban.toString());
    }

    @Test
    @DisplayName("Should throw exception for null IBAN")
    void shouldThrowExceptionForNullIBAN() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new IBAN(null)
        );
        assertEquals(FORMAT_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty IBAN")
    void shouldThrowExceptionForEmptyIBAN() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new IBAN("")
        );
        assertEquals(FORMAT_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for blank IBAN")
    void shouldThrowExceptionForBlankIBAN() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new IBAN("   ")
        );
        assertEquals(FORMAT_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid IBAN format or length")
    void shouldThrowExceptionForInvalidIBANFormat() {
        String invalidIBAN = "INVALIDIBAN123";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new IBAN(invalidIBAN)
        );
        if (invalidIBAN.length() < 15 || invalidIBAN.length() > 34) {
            assertEquals(LENGTH_ERROR, exception.getMessage());
        } else {
            assertEquals(FORMAT_ERROR, exception.getMessage());
        }
    }

    @Test
    @DisplayName("Should accept valid IBAN formats")
    void shouldAcceptValidIBANFormats() {
        String[] validIBANs = {
                "PT50123456789012345678901",
                "DE89370400440532013000",
                "GB29NWBK60161331926819",
                "FR1420041010050500013M02606",
                "ES9121000418450200051332"
        };

        for (String validIBAN : validIBANs) {
            assertDoesNotThrow(() -> new IBAN(validIBAN));
            IBAN iban = new IBAN(validIBAN);
            assertEquals(validIBAN, iban.getValue());
        }
    }

    @Test
    @DisplayName("Should reject invalid IBAN formats with specific messages")
    void shouldRejectInvalidIBANFormats() {
        String[] invalidIBANs = {
                "PT50 1234 5678 9012 3456 7890 1", // espaços não permitidos
                "123456789012345678901234",       // não começa com duas letras
                "P150123456789012345678901",      // só uma letra no início
                "PT5012345678901234567890@",      // caractere inválido
                "PT5012345678"                    // demasiado curto
        };

        for (String invalidIBAN : invalidIBANs) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new IBAN(invalidIBAN));
            if (invalidIBAN.length() < 15 || invalidIBAN.length() > 34) {
                assertEquals(LENGTH_ERROR, exception.getMessage());
            } else {
                assertEquals(FORMAT_ERROR, exception.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Should be equal when IBAN values are the same")
    void shouldBeEqualWhenIBANValuesAreTheSame() {
        String ibanValue = "PT50123456789012345678901";
        IBAN iban1 = new IBAN(ibanValue);
        IBAN iban2 = new IBAN(ibanValue);

        assertEquals(iban1.getValue(), iban2.getValue());
    }

    @Test
    @DisplayName("Should not be equal when IBAN values are different")
    void shouldNotBeEqualWhenIBANValuesAreDifferent() {
        IBAN iban1 = new IBAN("PT50123456789012345678901");
        IBAN iban2 = new IBAN("DE89370400440532013000");

        assertNotEquals(iban1.getValue(), iban2.getValue());
    }
}
