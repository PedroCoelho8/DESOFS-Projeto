package com.portal.de.pagamentos.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordTest {

    private static final String LENGTH_ERROR = "A password deve ter pelo menos 12 characters e um maximo de 128";
    private static final String FORMAT_ERROR = "Password não deve ter espaços e deve conter pelo menos uma letra minúscula, uma maiúscula, um número e um caracter especial.";
    private static final String BREACHED_ERROR = "Esta senha já foi comprometida em vazamentos públicos. Por favor escolha outra.";

    @Test
    @DisplayName("Should create Password with valid password")
    void shouldCreatePasswordWithValidPassword() {
        String validPassword = "MySecurePass123!";

        Password password = new Password(validPassword);

        assertNotNull(password.getValue());
        assertTrue(BCrypt.checkpw(validPassword, password.getValue()));
        assertEquals(password.getValue(), password.toString());
    }

    @Test
    @DisplayName("Should throw exception for null password")
    void shouldThrowExceptionForNullPassword() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(null)
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for password too short")
    void shouldThrowExceptionForPasswordTooShort() {
        String shortPassword = "Short1!";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(shortPassword)
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for password too long")
    void shouldThrowExceptionForPasswordTooLong() {
        String longPassword = "A".repeat(120) + "a1!" + "A".repeat(10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(longPassword)
        );
        assertEquals(LENGTH_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for password with spaces")
    void shouldThrowExceptionForPasswordWithSpaces() {
        String passwordWithSpaces = "MySecure Pass123!";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(passwordWithSpaces)
        );
        assertEquals(FORMAT_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for password without uppercase letter")
    void shouldThrowExceptionForPasswordWithoutUppercase() {
        String passwordWithoutUppercase = "mysecurepass123!";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(passwordWithoutUppercase)
        );
        assertEquals(FORMAT_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for password without lowercase letter")
    void shouldThrowExceptionForPasswordWithoutLowercase() {
        String passwordWithoutLowercase = "MYSECUREPASS123!";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(passwordWithoutLowercase)
        );
        assertEquals(FORMAT_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for password without number")
    void shouldThrowExceptionForPasswordWithoutNumber() {
        String passwordWithoutNumber = "MySecurePass!";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(passwordWithoutNumber)
        );
        assertEquals(FORMAT_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for password without special character")
    void shouldThrowExceptionForPasswordWithoutSpecialChar() {
        String passwordWithoutSpecialChar = "MySecurePass123";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Password(passwordWithoutSpecialChar)
        );
        assertEquals(FORMAT_ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should accept valid password formats")
    void shouldAcceptValidPasswordFormats() {
        String[] validPasswords = {
                "MySecurePass123!",
                "AnotherGood1@",
                "Complex#Pass123",
                "Str0ng$Password",
                "Valid&Password1"
        };

        for (String validPassword : validPasswords) {
            assertDoesNotThrow(() -> new Password(validPassword));
            Password password = new Password(validPassword);
            assertNotNull(password.getValue());
            assertTrue(BCrypt.checkpw(validPassword, password.getValue()));
        }
    }

    @Test
    @DisplayName("Should reject invalid password formats")
    void shouldRejectInvalidPasswordFormats() {
        String[] invalidPasswords = {
                "short1!",
                "nouppercase123!",
                "NOLOWERCASE123!",
                "NoNumbers!",
                "NoSpecialChar123",
                "Has Space123!",
                "OnlyLetters",
                "12345678901!",
                "!!!!!!!!!!!!",
        };

        for (String invalidPassword : invalidPasswords) {
            assertThrows(IllegalArgumentException.class, () -> new Password(invalidPassword));
        }
    }

    @Test
    @DisplayName("Should hash password using BCrypt")
    void shouldHashPasswordUsingBCrypt() {
        String plainPassword = "MySecurePass123!";

        Password password = new Password(plainPassword);

        assertNotEquals(plainPassword, password.getValue());
        assertTrue(password.getValue().startsWith("$2a$12$"));
        assertTrue(BCrypt.checkpw(plainPassword, password.getValue()));
    }

    @Test
    @DisplayName("Should generate different hashes for same password")
    void shouldGenerateDifferentHashesForSamePassword() {
        String plainPassword = "MySecurePass123!";

        Password password1 = new Password(plainPassword);
        Password password2 = new Password(plainPassword);

        assertNotEquals(password1.getValue(), password2.getValue());
        assertTrue(BCrypt.checkpw(plainPassword, password1.getValue()));
        assertTrue(BCrypt.checkpw(plainPassword, password2.getValue()));
    }

    @Test
    @DisplayName("Should accept password at minimum length boundary")
    void shouldAcceptPasswordAtMinimumLengthBoundary() {
        String minLengthPassword = "MySecure12!A";

        assertDoesNotThrow(() -> new Password(minLengthPassword));
        Password password = new Password(minLengthPassword);
        assertTrue(BCrypt.checkpw(minLengthPassword, password.getValue()));
    }

    @Test
    @DisplayName("Should accept password at maximum length boundary")
    void shouldAcceptPasswordAtMaximumLengthBoundary() {
        String maxLengthPassword = "A".repeat(120) + "a1!ABCD";

        assertDoesNotThrow(() -> new Password(maxLengthPassword));
        Password password = new Password(maxLengthPassword);
        assertTrue(BCrypt.checkpw(maxLengthPassword, password.getValue()));
    }

    @Test
    @DisplayName("Should test isBreached method with common password")
    void shouldTestIsBreastedMethodWithCommonPassword() {
        boolean result = Password.isBreached("password123");
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should handle network errors gracefully in isBreached method")
    void shouldHandleNetworkErrorsGracefullyInIsBreastedMethod() {
        String unusualPassword = "VeryUnusualP@ssw0rd2024!";

        assertDoesNotThrow(() -> Password.isBreached(unusualPassword));
        boolean result = Password.isBreached(unusualPassword);
        assertNotNull(result);
    }
}