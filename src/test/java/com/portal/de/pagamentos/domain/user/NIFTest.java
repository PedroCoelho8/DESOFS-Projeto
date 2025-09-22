package com.portal.de.pagamentos.domain.user;

import com.portal.de.pagamentos.domain.NIF;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NIFTest {

    private static final String NULL_ERROR = "NIF não pode ser nulo";
    private static final String LENGTH_ERROR = "NIF tem de ter 9 dígitos exatamente";
    private static final String NUMERIC_ERROR = "NIF tem de ter apenas dígitos";
    private static final String PREFIX_ERROR = "NIF tem de começar com 1, 2, 5, 6, 8, or 9";
    private static final String CHECKSUM_ERROR = "Invalid NIF checksum";

    @Test
    @DisplayName("Should create NIF with valid NIF number")
    void shouldCreateNifWithValidNifNumber() {
        String actualValidNif = "123456789";

        NIF nif = new NIF(actualValidNif);

        assertEquals(actualValidNif, nif.getValue());
    }

    @Test
    @DisplayName("Should throw exception for null NIF")
    void shouldThrowExceptionForNullNif() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new NIF(null)
        );
        assertEquals(NULL_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty NIF")
    void shouldThrowExceptionForEmptyNif() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new NIF("")
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for blank NIF")
    void shouldThrowExceptionForBlankNif() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new NIF("   ")
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for NIF too short")
    void shouldThrowExceptionForNifTooShort() {
        String shortNif = "12345678";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new NIF(shortNif)
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for NIF too long")
    void shouldThrowExceptionForNifTooLong() {
        String longNif = "1234567890";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new NIF(longNif)
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for NIF with non-numeric characters")
    void shouldThrowExceptionForNifWithNonNumericCharacters() {
        String[] expectedNumericError = {
                "12345678a",
                "abcdefghi",
                "12345@789"
        };

        for (String nif : expectedNumericError) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new NIF(nif)
            );
            assertEquals(NUMERIC_ERROR, exception.getMessage());
        }

        String[] expectedLengthError = {
                "123-456-78",
                "123 456 789",
                "123.456.789"
        };

        for (String nif : expectedLengthError) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new NIF(nif)
            );
            assertEquals(LENGTH_ERROR, exception.getMessage());
        }
    }


    @Test
    @DisplayName("Should throw exception for NIF with invalid first digit")
    void shouldThrowExceptionForNifWithInvalidFirstDigit() {
        String[] invalidFirstDigitNifs = {
                "012345678",
                "312345678",
                "412345678",
                "712345678"
        };

        for (String invalidFirstDigitNif : invalidFirstDigitNifs) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new NIF(invalidFirstDigitNif)
            );
            assertEquals(PREFIX_ERROR, exception.getMessage());
        }
    }


    @Test
    @DisplayName("Should trim whitespace from NIF")
    void shouldTrimWhitespaceFromNif() {
        String nifWithSpaces = "  123456789  ";
        String expectedNif = "123456789";

        NIF nif = new NIF(nifWithSpaces);

        assertEquals(expectedNif, nif.getValue());
    }


    @Test
    @DisplayName("Should throw exception for NIF with invalid checksum")
    void shouldThrowExceptionForNifWithInvalidChecksum() {
        String[] invalidChecksumNifs = {
                "123456780",
                "987654320"
        };

        for (String invalidChecksumNif : invalidChecksumNifs) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new NIF(invalidChecksumNif)
            );
            assertEquals(CHECKSUM_ERROR, exception.getMessage());
        }
    }

    @Test
    @DisplayName("Should create NIF with default constructor")
    void shouldCreateNifWithDefaultConstructor() {
        NIF nif = new NIF();

        assertNull(nif.getValue());
    }


    @Test
    @DisplayName("Should handle edge cases for checksum calculation")
    void shouldHandleEdgeCasesForChecksumCalculation() {
        assertDoesNotThrow(() -> {
            new NIF("123456789");
        });
    }

    @Test
    @DisplayName("Should be equal when NIF values are the same")
    void shouldBeEqualWhenNifValuesAreTheSame() {
        String nifValue = "123456789";
        NIF nif1 = new NIF(nifValue);
        NIF nif2 = new NIF(nifValue);

        assertEquals(nif1.getValue(), nif2.getValue());
    }

    @Test
    @DisplayName("Should not be equal when NIF values are different")
    void shouldNotBeEqualWhenNifValuesAreDifferent() {
        NIF nif1 = new NIF("123456789");
        NIF nif2 = new NIF("222222220");

        assertNotEquals(nif1.getValue(), nif2.getValue());
    }

    @Test
    @DisplayName("Should test boundary values for first digit validation")
    void shouldTestBoundaryValuesForFirstDigitValidation() {
        for (int i = 0; i <= 9; i++) {
            String testNif = i + "11111111";

            if (i == 1 || i == 2 || i == 5 || i == 6 || i == 8 || i == 9) {
                try {
                    new NIF(testNif);
                } catch (IllegalArgumentException e) {
                    assertEquals(CHECKSUM_ERROR, e.getMessage());
                }
            } else {
                IllegalArgumentException exception = assertThrows(
                        IllegalArgumentException.class,
                        () -> new NIF(testNif)
                );
                assertEquals(PREFIX_ERROR, exception.getMessage());
            }
        }
    }
}