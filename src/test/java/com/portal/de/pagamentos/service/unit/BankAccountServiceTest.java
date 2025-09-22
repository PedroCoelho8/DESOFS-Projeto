package com.portal.de.pagamentos.service.unit;

import com.portal.de.pagamentos.domain.bank_account.BankAccount;
import com.portal.de.pagamentos.domain.bank_account.dto.BankAccountDTO;
import com.portal.de.pagamentos.domain.bank_account.AccountType;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.repositories.irepositories.IBankAccountRepository;
import com.portal.de.pagamentos.repositories.irepositories.IUserRepository;
import com.portal.de.pagamentos.service.BankAccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class BankAccountServiceTest {

    private IBankAccountRepository bankAccountRepository;
    private IUserRepository userRepository;
    private BankAccountService service;

    @BeforeEach
    void setUp() {
        bankAccountRepository = mock(IBankAccountRepository.class);
        userRepository = mock(IUserRepository.class);
        service = new BankAccountService(bankAccountRepository, userRepository);
    }

    @Test
    void shouldCreateBankAccount() {
        // Mock do utilizador autenticado
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("joao@example.com");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        // Mock do utilizador encontrado
        User user = new User("João", "joao@example.com", "passJoao2025!", List.of("USER"), "912345678", "123456789", null);
        when(userRepository.findByEmail("joao@example.com")).thenReturn(Optional.of(user));

        // DTO de entrada
        BankAccountDTO dto = new BankAccountDTO();
        dto.setIban("PT50000201231234567890154");
        dto.setSwiftBic("BCPTPTPL");
        dto.setNifUser("123456789");
        dto.setAccountType(AccountType.CHECKING.toString());

        // Captura o que for enviado para o repositório
        ArgumentCaptor<BankAccount> captor = ArgumentCaptor.forClass(BankAccount.class);
        when(bankAccountRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Executa
        BankAccount created = service.create(dto);

        // Verifica
        verify(bankAccountRepository).save(captor.capture());
        assertThat(created.getIban().getValue()).isEqualTo(dto.getIban());
        assertThat(created.getSwiftBic().getValue()).isEqualTo(dto.getSwiftBic());
        assertThat(created.getNifUser().getValue()).isEqualTo(dto.getNifUser());
        assertThat(created.getCreatedBy()).isEqualTo(user);
    }

    @Test
    void shouldThrowWhenUserNotFoundInCreate() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("joao@example.com");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByEmail("joao@example.com")).thenReturn(Optional.empty());

        BankAccountDTO dto = new BankAccountDTO();

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Utilizador autenticado não encontrado");
    }

    @Test
    void shouldFindById() {
        BankAccount account = mock(BankAccount.class);
        when(bankAccountRepository.findById(1)).thenReturn(Optional.of(account));

        BankAccount found = service.findById(1);

        assertThat(found).isEqualTo(account);
    }

    @Test
    void shouldThrowWhenNotFoundById() {
        when(bankAccountRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Conta bancária com ID: 99 não encontrada");
    }

    @Test
    void shouldDeleteExistingAccount() {
        when(bankAccountRepository.existsById(1)).thenReturn(true);

        service.delete(1);

        verify(bankAccountRepository).deleteById(1);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentAccount() {
        when(bankAccountRepository.existsById(999)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Conta bancária com ID 999 não existe");
    }

    @Test
    void shouldUpdateAccount() {
        UUID userId = UUID.randomUUID();
        BankAccount existing = mock(BankAccount.class);
        when(bankAccountRepository.findById(1)).thenReturn(Optional.of(existing));

        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        BankAccountDTO dto = new BankAccountDTO();
        dto.setUpdatedById(userId.toString());
        dto.setIban("PT50000201231234567890154");     // IBAN válido
        dto.setSwiftBic("BCPTPTPL");                   // SWIFT válido
        dto.setNifUser("123456789");                   // NIF válido
        dto.setAccountType(AccountType.CHECKING.toString()); // Tipo de conta válido

        when(bankAccountRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BankAccount result = service.update(1, dto);

        verify(bankAccountRepository).save(existing);
        verify(userRepository).findById(userId);
        verify(bankAccountRepository).findById(1);
        assertThat(result).isEqualTo(existing);
    }


    @Test
    void shouldThrowWhenUpdateUserNotFound() {
        UUID userId = UUID.randomUUID();

        BankAccount existing = mock(BankAccount.class);
        when(bankAccountRepository.findById(1)).thenReturn(Optional.of(existing));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        BankAccountDTO dto = new BankAccountDTO();
        dto.setUpdatedById(userId.toString());

        assertThatThrownBy(() -> service.update(1, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Utilizador com ID: " + userId + " não encontrado");
    }
}
