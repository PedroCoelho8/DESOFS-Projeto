package com.portal.de.pagamentos.service;

import com.portal.de.pagamentos.domain.payment.dto.PaymentDTO;
import com.portal.de.pagamentos.domain.payment.dto.UpdatePaymentStateDTO;
import com.portal.de.pagamentos.domain.payment.Payment;
import com.portal.de.pagamentos.domain.payment.PaymentState;
import com.portal.de.pagamentos.domain.payment_document.PaymentDocument;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.exceptions.*;
import com.portal.de.pagamentos.repositories.irepositories.*;
import com.portal.de.pagamentos.service.iservices.IBankAccountService;
import com.portal.de.pagamentos.service.iservices.IPaymentService;
import com.portal.de.pagamentos.service.factory.PaymentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService implements IPaymentService {

    private IPaymentRepository paymentRepository;

    private IPaymentDocumentRepository paymentDocumentRepository;

    private IBankAccountRepository bankAccountRepository;

    private IUserRepository userRepository;

    public PaymentService(@Qualifier("IPaymentRepository") IPaymentRepository iPaymentRepository,
                          IPaymentDocumentRepository iPaymentDocumentRepository,
                          @Qualifier("IBankAccountRepository") IBankAccountRepository iBankAccountRepository,
                          IUserRepository iUserRepository) {
        this.paymentRepository = iPaymentRepository;
        this.paymentDocumentRepository = iPaymentDocumentRepository;
        this.bankAccountRepository = iBankAccountRepository;
        this.userRepository = iUserRepository;
    }

    @Override
    public Payment create(PaymentDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException("Não pode criar pagamento com id já definido");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        var userCreator = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(GeneralException.USER_NOT_FOUND));

        String role = userCreator.getRoles().get(0).toString();

        var userBankAccounts = this.bankAccountRepository.findByCreatedBy(userCreator);

        if (userBankAccounts.isEmpty()) {
            throw new BankAccountNotFound("Conta bancária do utilizador não encontrada");
        }

        var userBankAccount = userBankAccounts.get(0);

        if ("SENDERENTITY".equalsIgnoreCase(role)) {
            dto.setSenderBankAccountIban(userBankAccount.getIban().getValue());
        } else if ("RECEIVERENTITY".equalsIgnoreCase(role)) {
            dto.setReceiverBankAccountIban(userBankAccount.getIban().getValue());
        } else {
            throw new RoleUnidentifiedException("Role do utilizador inválida: " + role);
        }

        Map<String, PaymentDocument> documentMap = paymentDocumentRepository.findAll().stream()
                .collect(Collectors.toMap(doc -> doc.getId().toString(), doc -> doc));

        var sender = this.bankAccountRepository.findByIban_Value(dto.getSenderBankAccountIban())
                .orElseThrow(() -> new BankAccountNotFound("Conta bancária remetente não encontrada"));

        var receiver = this.bankAccountRepository.findByIban_Value(dto.getReceiverBankAccountIban())
                .orElseThrow(() -> new BankAccountNotFound("Conta bancária destinatária não encontrada"));

        Payment payment = PaymentFactory.createPayment(dto, documentMap, sender, receiver, userCreator);
        return this.paymentRepository.save(payment);
    }



    @Override
    public Payment send(UpdatePaymentStateDTO dto) {
        Payment payment = this.paymentRepository.findById(dto.getPaymentId())
                .orElseThrow(() -> new PaymentNotFoundException("Pagamento não encontrado", null));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String actingUsername = authentication.getName();

        User actingUser = this.userRepository.findByEmail(actingUsername)
                .orElseThrow(() -> new UserNotFoundException(GeneralException.USER_NOT_FOUND));

        if (!payment.getSenderBankAccount().getCreatedBy().equals(actingUser)) {
            throw new NotAuthorizedException("Utilizador não é o remetente autorizado");
        }

        if (payment.getState() != PaymentState.PENDING) {
            throw new NotPendingPaymentException("Pagamento não está em estado PENDING");
        }

        // Atualiza o estado para PAYED e persiste a mudança do pagamento
        payment.setState(PaymentState.PAYED);
        payment.setUpdatedBy(actingUser);
        Payment updatedPayment = this.paymentRepository.save(payment);

        // Cria o PaymentDocument utilizando os valores do DTO do body
        PaymentDocument document = new PaymentDocument();
        // Extraindo os NIFs dos usuários das contas (remetente e destinatário)
        document.setNifSender(payment.getSenderBankAccount().getCreatedBy().getNif().getValue());
        document.setNifReceiver(payment.getReceiverBankAccount().getCreatedBy().getNif().getValue());
        document.setValue(dto.getValue());
        document.setCurrency(dto.getCurrency());
        document.setCreatedBy(actingUser);

        // Salva o documento
        PaymentDocument savedDocument = this.paymentDocumentRepository.save(document);

        // Associa o documento ao pagamento na coleção
        updatedPayment.getPaymentDocuments().put(savedDocument, savedDocument.getValue());
        return this.paymentRepository.save(updatedPayment);
    }

    @Override
    @PreAuthorize("!hasRole('GUEST')")
    public Payment receive(UpdatePaymentStateDTO dto) {
        Payment payment = this.paymentRepository.findById(dto.getPaymentId())
                .orElseThrow(() -> new PaymentNotFoundException("Pagamento não encontrado", null));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String actingUsername = authentication.getName();

        User actingUser = this.userRepository.findByEmail(actingUsername)
                .orElseThrow(() -> new UserNotFoundException(GeneralException.USER_NOT_FOUND));

        if (!payment.getReceiverBankAccount().getCreatedBy().equals(actingUser)) {
            throw new UserNotDestined("Utilizador não é o destinatário autorizado", null);
        }

        if (payment.getState() == PaymentState.PAYED) {
            payment.setState(PaymentState.CONFIRMED_TO_BE_PAID);
        } else {
            throw new NotPendingPaymentException("Pagamento não está em estado PAYED");
        }

        payment.setUpdatedBy(actingUser);

        return this.paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        return this.paymentRepository.findAll();
    }

    @Override
    public List<Payment> getUserPayments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilizador não encontrado"));
        UUID userId = user.getId();

        return this.paymentRepository.findPaymentsByUser(userId);
    }
}
