package com.portal.de.pagamentos.service;

import com.portal.de.pagamentos.domain.bank_account.BankAccount;
import com.portal.de.pagamentos.domain.bank_account.dto.BankAccountDTO;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.repositories.irepositories.IBankAccountRepository;
import com.portal.de.pagamentos.repositories.irepositories.IUserRepository;
import com.portal.de.pagamentos.service.iservices.IBankAccountService;
import com.portal.de.pagamentos.service.factory.BankAccountFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BankAccountService implements IBankAccountService {

    private final IBankAccountRepository bankAccountRepository;
    private final IUserRepository userRepository;

    @Autowired
    public BankAccountService(
            IBankAccountRepository bankAccountRepository,
            IUserRepository userRepository
    ) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BankAccount create(BankAccountDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User creator = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilizador autenticado não encontrado"));

        BankAccount bankAccount = BankAccountFactory.fromDTO(dto, creator);
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount findById(Integer id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta bancária com ID: " + id + " não encontrada"));
    }

    @Override
    public BankAccount update(Integer id, BankAccountDTO dto) {
        BankAccount existing = findById(id);

        User updatedBy = userRepository.findById(UUID.fromString(dto.getUpdatedById()))
                .orElseThrow(() -> new IllegalArgumentException("Utilizador com ID: " + dto.getUpdatedById() + " não encontrado"));

        BankAccountFactory.updateFromDTO(existing, dto, updatedBy);
        return bankAccountRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if (!bankAccountRepository.existsById(id)) {
            throw new IllegalArgumentException("Conta bancária com ID " + id + " não existe");
        }
        bankAccountRepository.deleteById(id);
    }
}
