package com.portal.de.pagamentos.service.unit;

import com.portal.de.pagamentos.domain.payment.dto.PaymentDTO;
import com.portal.de.pagamentos.domain.payment.dto.UpdatePaymentStateDTO;
import com.portal.de.pagamentos.domain.payment.Payment;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.domain.bank_account.BankAccount;
import com.portal.de.pagamentos.repositories.irepositories.*;
import com.portal.de.pagamentos.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private IPaymentRepository paymentRepository;

    @Mock
    private IPaymentDocumentRepository paymentDocumentRepository;

    @Mock
    private IBankAccountRepository bankAccountRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private User mockUser;

    @BeforeEach
    void setupSecurityContext() {
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setEmail("user@example.com");
        mockUser.setRoles(List.of("SENDERENTITY"));

        lenient().when(authentication.getName()).thenReturn("user@example.com");
        lenient().when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUser));
    }

    @Test
    void create_WithIdSet_ShouldThrowException() {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(UUID.randomUUID().toString());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> paymentService.create(dto));
        assertEquals("Não pode criar pagamento com id já definido", ex.getMessage());
    }

    @Test
    void create_WhenUserHasNoBankAccount_ShouldThrow() {
        PaymentDTO dto = new PaymentDTO();

        when(bankAccountRepository.findByCreatedBy(mockUser)).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.create(dto));
        assertEquals("Conta bancária do utilizador não encontrada", ex.getMessage());
    }

    @Test
    void create_WhenValid_ShouldSavePayment() {
        PaymentDTO dto = new PaymentDTO();
        dto.setSenderBankAccountIban("PT50000201235678901578901");
        dto.setReceiverBankAccountIban("PT50000201235678901578902");
        dto.setState("PENDING");

        var senderBankAccount = mockBankAccount(mockUser, "PT50000201235678901578901");
        var receiverBankAccount = mockBankAccount(new User(), "PT50000201235678901578902");

        when(bankAccountRepository.findByCreatedBy(mockUser)).thenReturn(List.of(senderBankAccount));
        when(bankAccountRepository.findByIban_Value("PT50000201235678901578901")).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findByIban_Value("PT50000201235678901578902")).thenReturn(Optional.of(receiverBankAccount));
        when(paymentDocumentRepository.findAll()).thenReturn(Collections.emptyList());
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));

        Payment payment = paymentService.create(dto);

        assertNotNull(payment);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void send_WhenPaymentNotFound_ShouldThrow() {
        UpdatePaymentStateDTO dto = new UpdatePaymentStateDTO();
        dto.setPaymentId(UUID.randomUUID());

        when(paymentRepository.findById(dto.getPaymentId())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.send(dto));
        assertEquals("Pagamento não encontrado", ex.getMessage());
    }

    private BankAccount mockBankAccount(User user, String ibanValue) {
        var iban = new com.portal.de.pagamentos.domain.bank_account.IBAN(ibanValue);
        var bankAccount = mock(BankAccount.class);
        lenient().when(bankAccount.getCreatedBy()).thenReturn(user);
        lenient().when(bankAccount.getIban()).thenReturn(iban);
        return bankAccount;
    }
}
