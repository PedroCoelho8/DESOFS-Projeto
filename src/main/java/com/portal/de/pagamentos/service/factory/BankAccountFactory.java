package com.portal.de.pagamentos.service.factory;

import com.portal.de.pagamentos.domain.NIF;
import com.portal.de.pagamentos.domain.bank_account.*;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.domain.bank_account.dto.BankAccountDTO;

public class BankAccountFactory {

    public static BankAccount fromDTO(BankAccountDTO dto, User createdBy) {
        return new BankAccount(
                new IBAN(dto.getIban()),
                new SwiftBic(dto.getSwiftBic()),
                createdBy.getNif(),
                AccountType.getByCode(dto.getAccountType()),
                createdBy
        );
    }

    public static void updateFromDTO(BankAccount bankAccount, BankAccountDTO dto, User updatedBy) {
        bankAccount.setIban(new IBAN(dto.getIban()));
        bankAccount.setSwiftBic(new SwiftBic(dto.getSwiftBic()));
        bankAccount.setNifUser(new NIF(dto.getNifUser()));
        bankAccount.setAccountType(AccountType.getByCode(dto.getAccountType()));
        bankAccount.setUpdatedBy(updatedBy);
    }
}
