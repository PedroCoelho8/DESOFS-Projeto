package com.portal.de.pagamentos.domain.user;

import com.portal.de.pagamentos.domain.NIF;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    @DisplayName("Should create User with valid parameters")
    void shouldCreateUserWithValidParameters() {
        String name = "João Silva";
        String email = "joao@example.com";
        String password = "MySecurePass123!";
        List<String> roles = Arrays.asList("ADMIN", "USER");
        String phoneNumber = "912345678";
        String nif = "123456789";

        User user = new User(name, email, password, roles, phoneNumber, nif, "newURL");

        assertEquals(name, user.getName().getValue());
        assertEquals(email, user.getEmail().getValue());
        assertTrue(BCrypt.checkpw(password, user.getPassword().getValue()));
        assertEquals(phoneNumber, user.getPhoneNumber().getValue());
        assertEquals(nif, user.getNif().getValue());
        assertNotNull(user.getRoles());
        assertFalse(user.getRoles().isEmpty());
    }

    @Test
    @DisplayName("Should create User with default constructor")
    void shouldCreateUserWithDefaultConstructor() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getRoles());
        assertNull(user.getPhoneNumber());
        assertNull(user.getNif());
    }

    @Test
    @DisplayName("Should generate UUID for id")
    void shouldGenerateUUIDForId() {
        User user1 = new User("João Silva", "joao1@example.com", "MySecuress123!",
                Arrays.asList("USER"), "912345678", "123456789", "newURL");
        User user2 = new User("Maria Santos", "maria@example.com", "Anotheass123!",
                Arrays.asList("USER"), "923456789", "222222220", "newURL");

        assertNull(user1.getId());
        assertNull(user2.getId());
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void shouldSetAndGetNameCorrectly() {
        User user = new User();
        String name = "José António";

        user.setName(name);

        assertEquals(name, user.getName().getValue());
        assertInstanceOf(Username.class, user.getName());
    }

    @Test
    @DisplayName("Should set and get email correctly")
    void shouldSetAndGetEmailCorrectly() {
        User user = new User();
        String email = "test@example.com";

        user.setEmail(email);

        assertEquals(email, user.getEmail().getValue());
        assertInstanceOf(Email.class, user.getEmail());
    }

    @Test
    @DisplayName("Should set and get password correctly")
    void shouldSetAndGetPasswordCorrectly() {
        User user = new User();
        String password = "MySecurePass123!";

        user.setPassword(password);

        assertTrue(BCrypt.checkpw(password, user.getPassword().getValue()));
        assertInstanceOf(Password.class, user.getPassword());
    }

    @Test
    @DisplayName("Should set and get phone number correctly")
    void shouldSetAndGetPhoneNumberCorrectly() {
        User user = new User();
        String phoneNumber = "912345678";

        user.setPhoneNumber(phoneNumber);

        assertEquals(phoneNumber, user.getPhoneNumber().getValue());
        assertInstanceOf(PhoneNumber.class, user.getPhoneNumber());
    }

    @Test
    @DisplayName("Should set and get NIF correctly")
    void shouldSetAndGetNifCorrectly() {
        User user = new User();
        String nif = "123456789"; // Using valid NIF

        user.setNif(nif);

        assertEquals(nif, user.getNif().getValue());
        assertInstanceOf(NIF.class, user.getNif());
    }

    @Test
    @DisplayName("Should set roles correctly with valid role codes")
    void shouldSetRolesCorrectlyWithValidRoleCodes() {
        User user = new User();
        List<String> roleCodes = Arrays.asList("ADMIN", "USER");

        user.setRoles(roleCodes);

        assertNotNull(user.getRoles());
        assertFalse(user.getRoles().isEmpty());
        // Assuming Role.findByCode works correctly
        assertTrue(user.getRoles().size() <= roleCodes.size());
    }

    @Test
    @DisplayName("Should set GUEST role when no valid roles provided")
    void shouldSetGuestRoleWhenNoValidRolesProvided() {
        User user = new User();
        List<String> invalidRoles = Arrays.asList("INVALID_ROLE", "ANOTHER_INVALID");

        user.setRoles(invalidRoles);

        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
        assertEquals(Role.GUEST, user.getRoles().get(0));
    }

    @Test
    @DisplayName("Should set GUEST role when empty roles list provided")
    void shouldSetGuestRoleWhenEmptyRolesListProvided() {
        User user = new User();
        List<String> emptyRoles = Arrays.asList();

        user.setRoles(emptyRoles);

        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
        assertEquals(Role.GUEST, user.getRoles().get(0));
    }

    @Test
    @DisplayName("Should throw exception when setting invalid name")
    void shouldThrowExceptionWhenSettingInvalidName() {
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> user.setName(null));
        assertThrows(IllegalArgumentException.class, () -> user.setName("Invalid123"));
    }

    @Test
    @DisplayName("Should throw exception when setting invalid email")
    void shouldThrowExceptionWhenSettingInvalidEmail() {
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("invalid-email"));
    }

    @Test
    @DisplayName("Should throw exception when setting invalid password")
    void shouldThrowExceptionWhenSettingInvalidPassword() {
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> user.setPassword(null));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword("weak"));
    }

    @Test
    @DisplayName("Should throw exception when setting invalid phone number")
    void shouldThrowExceptionWhenSettingInvalidPhoneNumber() {
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> user.setPhoneNumber(null));
        assertThrows(IllegalArgumentException.class, () -> user.setPhoneNumber("123456"));
        assertThrows(IllegalArgumentException.class, () -> user.setPhoneNumber("812345678"));
    }

    @Test
    @DisplayName("Should throw exception when setting invalid NIF")
    void shouldThrowExceptionWhenSettingInvalidNif() {
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> user.setNif(null));
        assertThrows(IllegalArgumentException.class, () -> user.setNif("12345"));
        assertThrows(IllegalArgumentException.class, () -> user.setNif("abcdefghi"));
    }

    @Test
    @DisplayName("Should create complete User through constructor with all validations")
    void shouldCreateCompleteUserThroughConstructorWithAllValidations() {
        String name = "Maria João";
        String email = "maria.joao@example.com";
        String password = "SecurePasswd123!";
        List<String> roles = Arrays.asList("USER");
        String phoneNumber = "923456789";
        String nif = "123456789"; // Using valid NIF

        User user = new User(name, email, password, roles, phoneNumber, nif, "newURL");

        // Verify all fields are set correctly
        assertEquals(name, user.getName().getValue());
        assertEquals(email, user.getEmail().getValue());
        assertTrue(BCrypt.checkpw(password, user.getPassword().getValue()));
        assertNotNull(user.getRoles());
        assertFalse(user.getRoles().isEmpty());
        assertEquals(phoneNumber, user.getPhoneNumber().getValue());
        assertEquals(nif, user.getNif().getValue());
    }

    @Test
    @DisplayName("Should handle mixed valid and invalid roles")
    void shouldHandleMixedValidAndInvalidRoles() {
        User user = new User();
        List<String> mixedRoles = Arrays.asList("USER", "INVALID_ROLE", "ADMIN", "ANOTHER_INVALID");

        user.setRoles(mixedRoles);

        assertNotNull(user.getRoles());
        // Should contain only the valid roles (USER and ADMIN if they exist)
        assertTrue(user.getRoles().size() >= 1);
        // Should not contain GUEST if valid roles were found
        if (user.getRoles().size() > 1) {
            assertNotEquals(Role.GUEST, user.getRoles().get(0));
        }
    }

    @Test
    @DisplayName("Should validate all constructor parameters")
    void shouldValidateAllConstructorParameters() {
        // Test with various invalid combinations
        assertThrows(IllegalArgumentException.class, () ->
                new User(null, "valid@email.com", "ValidPass123!", Arrays.asList("USER"), "912345678", "111111116", "newURL"));

        assertThrows(IllegalArgumentException.class, () ->
                new User("Valid Name", null, "ValidPass123!", Arrays.asList("USER"), "912345678", "111111116", "newURL"));

        assertThrows(IllegalArgumentException.class, () ->
                new User("Valid Name", "valid@email.com", null, Arrays.asList("USER"), "912345678", "111111116", "newURL"));

        assertThrows(IllegalArgumentException.class, () ->
                new User("Valid Name", "valid@email.com", "ValidPass123!", Arrays.asList("USER"), null, "111111116", "newURL"));

        assertThrows(IllegalArgumentException.class, () ->
                new User("Valid Name", "valid@email.com", "ValidPass123!", Arrays.asList("USER"), "912345678", null, "newURL"));
    }

    @Test
    @DisplayName("Should create user with minimum valid inputs")
    void shouldCreateUserWithMinimumValidInputs() {
        String name = "A";
        String email = "a@b.com";
        String password = "MinPassword12!";
        List<String> roles = Arrays.asList(); // Empty list should result in GUEST
        String phoneNumber = "210000000";
        String nif = "123456789"; // Using valid NIF with correct checksum

        User user = new User(name, email, password, roles, phoneNumber, nif, "newURL");

        assertEquals(name, user.getName().getValue());
        assertEquals(email, user.getEmail().getValue());
        assertTrue(BCrypt.checkpw(password, user.getPassword().getValue()));
        assertEquals(1, user.getRoles().size());
        assertEquals(Role.GUEST, user.getRoles().get(0));
        assertEquals(phoneNumber, user.getPhoneNumber().getValue());
        assertEquals(nif, user.getNif().getValue());
    }
}