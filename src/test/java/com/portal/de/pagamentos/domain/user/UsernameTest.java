package com.portal.de.pagamentos.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsernameTest {

    private static final String ERROR = "Username must contain only letters, spaces, or hyphens.";

    @Test
    @DisplayName("Should create Username with valid username")
    void shouldCreateUsernameWithValidUsername() {
        String validUsername = "João Silva";

        Username username = new Username(validUsername);

        assertEquals(validUsername, username.getValue());
        assertEquals(validUsername, username.toString());
    }

    @Test
    @DisplayName("Should throw exception for null username")
    void shouldThrowExceptionForNullUsername() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Username(null)
        );
        assertEquals(ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty username")
    void shouldThrowExceptionForEmptyUsername() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Username("")
        );
        assertEquals(ERROR, exception.getMessage());
    }

    @Test
    @DisplayName("Should accept valid username formats")
    void shouldAcceptValidUsernameFormats() {
        String[] validUsernames = {
                "João",
                "Maria Silva",
                "José-Pedro",
                "Ana Luísa",
                "António",
                "José Maria dos Santos",
                "Marie-Claire",
                "Jean-Luc",
                "Françoise",
                "André",
                "Céline",
                "Björk",
                "José María",
                "François",
                "Åse",
                "Øystein",
                "A B C",
                "First Last",
                "First-Middle Last",
                "Name with Multiple Spaces",
                "Multiple-Hyphens-Name"
        };

        for (String validUsername : validUsernames) {
            assertDoesNotThrow(() -> new Username(validUsername));
            Username username = new Username(validUsername);
            assertEquals(validUsername, username.getValue());
        }
    }

    @Test
    @DisplayName("Should reject invalid username formats")
    void shouldRejectInvalidUsernameFormats() {
        String[] invalidUsernames = {
                "John123",
                "user@domain",
                "user.name",
                "user_name",
                "user#name",
                "user$name",
                "user%name",
                "user&name",
                "user*name",
                "user+name",
                "user=name",
                "user(name)",
                "user[name]",
                "user{name}",
                "user|name",
                "user\\name",
                "user/name",
                "user<name>",
                "user?name",
                "user!name",
                "user:name",
                "user;name",
                "user,name",
                "user\"name\"",
                "user'name'",
                "user`name`"
        };

        for (String invalidUsername : invalidUsernames) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Username(invalidUsername)
            );
            assertEquals(ERROR, exception.getMessage());
        }
    }

    @Test
    @DisplayName("Should accept usernames with accented characters")
    void shouldAcceptUsernamesWithAccentedCharacters() {
        String[] accentedUsernames = {
                "José",
                "María",
                "François",
                "Björn",
                "Håkan",
                "Søren",
                "Amélie",
                "Céline",
                "Agnès",
                "Renée",
                "André",
                "Andrés",
                "Ángel",
                "Ülrich",
                "Müller",
                "Nöel",
                "Jürgen",
                "Åse",
                "Øyvind",
                "Åström"
        };

        for (String accentedUsername : accentedUsernames) {
            assertDoesNotThrow(() -> new Username(accentedUsername));
            Username username = new Username(accentedUsername);
            assertEquals(accentedUsername, username.getValue());
        }
    }

    @Test
    @DisplayName("Should accept usernames with spaces")
    void shouldAcceptUsernamesWithSpaces() {
        String[] usernamesWithSpaces = {
                "First Last",
                "First Middle Last",
                "First    Multiple    Spaces",
                " Leading Space",
                "Trailing Space ",
                " Both Sides ",
                "Multiple   Consecutive   Spaces"
        };

        for (String usernameWithSpaces : usernamesWithSpaces) {
            assertDoesNotThrow(() -> new Username(usernameWithSpaces));
            Username username = new Username(usernameWithSpaces);
            assertEquals(usernameWithSpaces, username.getValue());
        }
    }

    @Test
    @DisplayName("Should accept usernames with hyphens")
    void shouldAcceptUsernamesWithHyphens() {
        String[] usernamesWithHyphens = {
                "Jean-Pierre",
                "Marie-Claire",
                "José-María",
                "First-Last",
                "Multi-Hyphen-Name",
                "-Leading Hyphen",
                "Trailing Hyphen-",
                "-Both-",
                "Multiple--Hyphens",
                "Hyphen-And Space"
        };

        for (String usernameWithHyphens : usernamesWithHyphens) {
            assertDoesNotThrow(() -> new Username(usernameWithHyphens));
            Username username = new Username(usernameWithHyphens);
            assertEquals(usernameWithHyphens, username.getValue());
        }
    }

    @Test
    @DisplayName("Should accept usernames with combination of spaces and hyphens")
    void shouldAcceptUsernamesWithCombinationOfSpacesAndHyphens() {
        String[] combinedUsernames = {
                "Jean-Pierre Dubois",
                "Marie-Claire de Silva",
                "José María Santos-Pérez",
                "Ana-Luísa dos Santos",
                "First-Middle Last Name",
                "Complex-Name With Spaces"
        };

        for (String combinedUsername : combinedUsernames) {
            assertDoesNotThrow(() -> new Username(combinedUsername));
            Username username = new Username(combinedUsername);
            assertEquals(combinedUsername, username.getValue());
        }
    }

    @Test
    @DisplayName("Should be equal when username values are the same")
    void shouldBeEqualWhenUsernameValuesAreTheSame() {
        String usernameValue = "João Silva";
        Username username1 = new Username(usernameValue);
        Username username2 = new Username(usernameValue);

        assertEquals(username1.getValue(), username2.getValue());
    }

    @Test
    @DisplayName("Should not be equal when username values are different")
    void shouldNotBeEqualWhenUsernameValuesAreDifferent() {
        Username username1 = new Username("João Silva");
        Username username2 = new Username("Maria Santos");

        assertNotEquals(username1, username2);
    }

    @Test
    @DisplayName("Should accept single character username")
    void shouldAcceptSingleCharacterUsername() {
        String[] singleCharUsernames = {"A", "É", "Ø", "a", "ñ"};

        for (String singleChar : singleCharUsernames) {
            assertDoesNotThrow(() -> new Username(singleChar));
            Username username = new Username(singleChar);
            assertEquals(singleChar, username.getValue());
        }
    }

    @Test
    @DisplayName("Should accept username with only spaces")
    void shouldAcceptUsernameWithOnlySpaces() {
        String spacesOnly = "   ";

        assertDoesNotThrow(() -> new Username(spacesOnly));
        Username username = new Username(spacesOnly);
        assertEquals(spacesOnly, username.getValue());
    }

    @Test
    @DisplayName("Should accept username with only hyphens")
    void shouldAcceptUsernameWithOnlyHyphens() {
        String hyphensOnly = "---";

        assertDoesNotThrow(() -> new Username(hyphensOnly));
        Username username = new Username(hyphensOnly);
        assertEquals(hyphensOnly, username.getValue());
    }

    @Test
    @DisplayName("Should test case sensitivity")
    void shouldTestCaseSensitivity() {
        String[] caseVariations = {
                "UPPERCASE",
                "lowercase",
                "MixedCase",
                "camelCase",
                "PascalCase"
        };

        for (String caseVariation : caseVariations) {
            assertDoesNotThrow(() -> new Username(caseVariation));
            Username username = new Username(caseVariation);
            assertEquals(caseVariation, username.getValue());
        }
    }
}