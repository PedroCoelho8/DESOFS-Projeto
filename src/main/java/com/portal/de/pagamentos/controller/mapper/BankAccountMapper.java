package com.portal.de.pagamentos.controller.mapper;

import com.portal.de.pagamentos.domain.NIF;
import com.portal.de.pagamentos.domain.bank_account.*;
import com.portal.de.pagamentos.domain.bank_account.dto.BankAccountDTO;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.repositories.irepositories.IUserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankAccountMapper {

    private final IUserRepository userRepository;

    public BankAccountMapper(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BankAccount toEntity(BankAccountDTO dto) {
        User createdBy = userRepository.findById(UUID.fromString(dto.getCreatedById()))
                .orElseThrow(() -> new IllegalArgumentException("Utilizador criador não encontrado"));
        User updatedBy = dto.getUpdatedById() != null
                ? userRepository.findById(UUID.fromString(dto.getUpdatedById()))
                .orElseThrow(() -> new IllegalArgumentException("Utilizador atualizador não encontrado"))
                : null;

        return new BankAccount(
                new IBAN(dto.getIban()),
                new SwiftBic(dto.getSwiftBic()),
                new NIF(dto.getNifUser()),
                AccountType.getByCode(dto.getAccountType()),
                createdBy
        );
    }

    public BankAccountDTO toDTO(BankAccount entity) {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setIban(entity.getIban().getValue());
        dto.setSwiftBic(entity.getSwiftBic().getValue());
        dto.setNifUser(entity.getNifUser().getValue());
        dto.setAccountType(entity.getAccountType().getCode());
        dto.setCreatedById(entity.getCreatedBy().getId().toString());
        dto.setUpdatedById(entity.getUpdatedBy() != null ? entity.getUpdatedBy().getId().toString() : null);
        dto.setDtCreated(entity.getDtCreated());
        dto.setDtUpdated(entity.getDtUpdated());
        return dto;
    }
}
