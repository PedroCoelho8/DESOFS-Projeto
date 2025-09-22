package com.portal.de.pagamentos.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneNumberTest {

    private static final String LENGTH_ERROR = "O número de telemóvel deve ter 9 dígitos";
    private static final String PREFIX_ERROR = "O número de telemóvel deve começar por 21, 22, 91, 92, 93 ou 96";

    @Test
    @DisplayName("Should create PhoneNumber with valid phone number")
    void shouldCreatePhoneNumberWithValidPhoneNumber() {
        String validPhone = "912345678";

        PhoneNumber phoneNumber = new PhoneNumber(validPhone);

        assertEquals(validPhone, phoneNumber.getValue());
        assertEquals(validPhone, phoneNumber.toString());
    }

    @Test
    @DisplayName("Should throw exception for null phone number")
    void shouldThrowExceptionForNullPhoneNumber() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PhoneNumber(null)
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty phone number")
    void shouldThrowExceptionForEmptyPhoneNumber() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PhoneNumber("")
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for blank phone number")
    void shouldThrowExceptionForBlankPhoneNumber() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PhoneNumber("   ")
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for phone number too short")
    void shouldThrowExceptionForPhoneNumberTooShort() {
        String shortPhone = "12345678";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PhoneNumber(shortPhone)
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for phone number too long")
    void shouldThrowExceptionForPhoneNumberTooLong() {
        String longPhone = "1234567890";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PhoneNumber(longPhone)
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for phone number with non-numeric characters")
    void shouldThrowExceptionForPhoneNumberWithNonNumericCharacters() {
        String[] nonNumericPhones = {
                "91234567a",
                "912-34567",
                "912 34567",
                "912.34567",
                "+12345678",
                "abcdefghi"
        };

        for (String nonNumericPhone : nonNumericPhones) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PhoneNumber(nonNumericPhone)
            );
            assertEquals(LENGTH_ERROR, exception.getMessage());
        }
    }

    @Test
    @DisplayName("Should throw exception for phone number with invalid prefix")
    void shouldThrowExceptionForPhoneNumberWithInvalidPrefix() {
        String[] invalidPrefixPhones = {
                "123456789",
                "111234567",
                "234567890",
                "812345678",
                "941234567",
                "971234567",
                "981234567"
        };

        for (String invalidPrefixPhone : invalidPrefixPhones) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PhoneNumber(invalidPrefixPhone)
            );
            assertEquals(PREFIX_ERROR, exception.getMessage());
        }
    }

    @Test
    @DisplayName("Should accept valid phone number formats")
    void shouldAcceptValidPhoneNumberFormats() {
        String[] validPhones = {
                "212345678",
                "223456789",
                "912345678",
                "923456789",
                "933456789",
                "963456789"
        };

        for (String validPhone : validPhones) {
            assertDoesNotThrow(() -> new PhoneNumber(validPhone));
            PhoneNumber phoneNumber = new PhoneNumber(validPhone);
            assertEquals(validPhone, phoneNumber.getValue());
        }
    }

    @Test
    @DisplayName("Should reject invalid phone number formats")
    void shouldRejectInvalidPhoneNumberFormats() {
        String[] invalidPhones = {
                "12345678",
                "1234567890",
                "812345678",
                "941234567",
                "971234567",
                "123456789",
                "234567890",
                "91234567a",
                "912-34567",
                "912 34567"
        };

        for (String invalidPhone : invalidPhones) {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(invalidPhone));
        }
    }

    @Test
    @DisplayName("Should be equal when phone number values are the same")
    void shouldBeEqualWhenPhoneNumberValuesAreTheSame() {
        String phoneValue = "912345678";
        PhoneNumber phone1 = new PhoneNumber(phoneValue);
        PhoneNumber phone2 = new PhoneNumber(phoneValue);

        assertEquals(phone1.getValue(), phone2.getValue());
    }

    @Test
    @DisplayName("Should not be equal when phone number values are different")
    void shouldNotBeEqualWhenPhoneNumberValuesAreDifferent() {
        PhoneNumber phone1 = new PhoneNumber("912345678");
        PhoneNumber phone2 = new PhoneNumber("923456789");

        assertNotEquals(phone1, phone2);
    }

    @Test
    @DisplayName("Should test all valid prefixes with exact 9 digits")
    void shouldTestAllValidPrefixesWithExact9Digits() {
        String[] validPrefixes = {"21", "22", "91", "92", "93", "96"};

        for (String prefix : validPrefixes) {
            String validPhone = prefix + "1234567";
            assertDoesNotThrow(() -> new PhoneNumber(validPhone));
            PhoneNumber phoneNumber = new PhoneNumber(validPhone);
            assertEquals(validPhone, phoneNumber.getValue());
            assertTrue(phoneNumber.getValue().startsWith(prefix));
        }
    }

    @Test
    @DisplayName("Should test boundary cases for valid prefixes")
    void shouldTestBoundaryCasesForValidPrefixes() {
        String[] validEdgeCases = {
                "210000000",
                "229999999",
                "910000000",
                "929999999",
                "930000000",
                "969999999"
        };

        for (String validEdgeCase : validEdgeCases) {
            assertDoesNotThrow(() -> new PhoneNumber(validEdgeCase));
            PhoneNumber phoneNumber = new PhoneNumber(validEdgeCase);
            assertEquals(validEdgeCase, phoneNumber.getValue());
        }
    }
}