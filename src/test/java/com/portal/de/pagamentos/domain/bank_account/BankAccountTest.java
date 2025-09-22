package com.portal.de.pagamentos.domain.bank_account;

import com.portal.de.pagamentos.domain.NIF;
import com.portal.de.pagamentos.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BankAccountTest {

    private IBAN validIban;
    private SwiftBic validSwiftBic;
    private NIF mockNif;
    private User creator;
    private User updater;

    @BeforeEach
    void setup() {
        validIban = new IBAN("PT50000201231234567890154");
        validSwiftBic = new SwiftBic("BCOMPTPLXXX");

        mockNif = mock(NIF.class);
        creator = mock(User.class);
        updater = mock(User.class);
    }

    @Test
    @DisplayName("Should create BankAccount with valid parameters")
    void shouldCreateBankAccount() {
        BankAccount account = new BankAccount(validIban, validSwiftBic, mockNif, AccountType.CHECKING, creator);

        assertNull(account.getId());
        assertEquals(validIban, account.getIban());
        assertEquals(validSwiftBic, account.getSwiftBic());
        assertEquals(mockNif, account.getNifUser());
        assertEquals(AccountType.CHECKING, account.getAccountType());
        assertEquals(creator, account.getCreatedBy());

        assertNull(account.getUpdatedBy());
        assertNull(account.getDtCreated());
        assertNull(account.getDtUpdated());
    }

    @Test
    @DisplayName("Should update dtCreated on persist")
    void shouldSetDtCreatedOnPersist() {
        BankAccount account = new BankAccount(validIban, validSwiftBic, mockNif, AccountType.CHECKING, creator);

        account.onCreate();

        assertNotNull(account.getDtCreated());
        LocalDateTime created = account.getDtCreated();

        assertTrue(created.isBefore(LocalDateTime.now()) || created.isEqual(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should update dtUpdated on update")
    void shouldSetDtUpdatedOnUpdate() {
        BankAccount account = new BankAccount(validIban, validSwiftBic, mockNif, AccountType.CHECKING, creator);

        account.onCreate();
        LocalDateTime created = account.getDtCreated();

        account.onUpdate();

        assertNotNull(account.getDtUpdated());
        LocalDateTime updated = account.getDtUpdated();

        assertTrue(updated.isAfter(created) || updated.isEqual(created));
    }

    @Test
    @DisplayName("Should set and get updatedBy")
    void shouldSetAndGetUpdatedBy() {
        BankAccount account = new BankAccount(validIban, validSwiftBic, mockNif, AccountType.CHECKING, creator);
        account.setUpdatedBy(updater);

        assertEquals(updater, account.getUpdatedBy());
    }

    @Test
    @DisplayName("Should allow changing IBAN, SwiftBic, NIF and AccountType")
    void shouldAllowChangingFields() {
        BankAccount account = new BankAccount(validIban, validSwiftBic, mockNif, AccountType.CHECKING, creator);

        IBAN newIban = new IBAN("PT50000201231234567890100");
        SwiftBic newSwift = new SwiftBic("DEUTDEFF");
        NIF newMockNif = mock(NIF.class);
        AccountType newType = AccountType.SAVINGS;

        account.setIban(newIban);
        account.setSwiftBic(newSwift);
        account.setNifUser(newMockNif);
        account.setAccountType(newType);

        assertEquals(newIban, account.getIban());
        assertEquals(newSwift, account.getSwiftBic());
        assertEquals(newMockNif, account.getNifUser());
        assertEquals(newType, account.getAccountType());
    }
}
