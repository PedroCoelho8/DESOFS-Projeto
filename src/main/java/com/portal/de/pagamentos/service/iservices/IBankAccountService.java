package com.portal.de.pagamentos.service.iservices;

import com.portal.de.pagamentos.domain.bank_account.BankAccount;
import com.portal.de.pagamentos.domain.bank_account.dto.BankAccountDTO;

import java.util.List;

public interface IBankAccountService {
    BankAccount create(BankAccountDTO dto);
    List<BankAccount> findAll();
    BankAccount findById(Integer id);
    BankAccount update(Integer id, BankAccountDTO dto);
    void delete(Integer id);
}
