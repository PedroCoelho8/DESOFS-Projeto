package com.portal.de.pagamentos.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    private static String ERROR = "Email é invalido. Deve conter [Alfanumericos]@[Alfanumericos].[com|pt]";

    @Test
    @DisplayName("Should create Email with valid email address")
    void shouldCreateEmailWithValidAddress() {
        String validEmail = "test@example.com";

        Email email = new Email(validEmail);

        assertEquals(validEmail, email.getValue());
        assertEquals(validEmail, email.toString());
    }

    @Test
    @DisplayName("Should throw exception for null email")
    void shouldThrowExceptionForNullEmail() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Email(null)
        );
        assertEquals(ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty email")
    void shouldThrowExceptionForEmptyEmail() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Email("")
        );
        assertEquals(ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for blank email")
    void shouldThrowExceptionForBlankEmail() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Email("   ")
        );
        assertEquals(ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid email format")
    void shouldThrowExceptionForInvalidEmailFormat() {
        String invalidEmail = "invalid-email";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Email(invalidEmail)
        );
        assertEquals(ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should accept valid email formats")
    void shouldAcceptValidEmailFormats() {
        String[] validEmails = {
                "test@example.com",
                "user.name@domain.co.uk",
                "user+tag@example.org",
                "123@example.com",
                "test.email@example-domain.com"
        };

        for (String validEmail : validEmails) {
            assertDoesNotThrow(() -> new Email(validEmail));
            Email email = new Email(validEmail);
            assertEquals(validEmail, email.getValue());
        }
    }

    @Test
    @DisplayName("Should reject invalid email formats")
    void shouldRejectInvalidEmailFormats() {
        String[] invalidEmails = {
                "plainaddress",
                "@missingdomain.com",
                "missing@.com",
                "missing@domain",
                "spaces in@email.com",
                "double@@domain.com",
                "trailing.dot@domain.com.",
                ".leading.dot@domain.com"
        };

        for (String invalidEmail : invalidEmails) {
            assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
        }
    }

    @Test
    @DisplayName("Should be equal when email values are the same")
    void shouldBeEqualWhenEmailValuesAreTheSame() {
        String emailValue = "test@example.com";
        Email email1 = new Email(emailValue);
        Email email2 = new Email(emailValue);

        assertEquals(email1.getValue(), email2.getValue());
    }

    @Test
    @DisplayName("Should not be equal when email values are different")
    void shouldNotBeEqualWhenEmailValuesAreDifferent() {
        Email email1 = new Email("test1@example.com");
        Email email2 = new Email("test2@example.com");

        assertNotEquals(email1, email2);
    }
}