package com.portal.de.pagamentos.controller;

import com.portal.de.pagamentos.controller.mapper.BankAccountMapper;
import com.portal.de.pagamentos.domain.bank_account.BankAccount;
import com.portal.de.pagamentos.domain.bank_account.dto.BankAccountDTO;
import com.portal.de.pagamentos.service.iservices.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankaccounts")
public class BankAccountController {

    private final IBankAccountService bankAccountService;
    private final BankAccountMapper bankAccountMapper;

    @Autowired
    public BankAccountController(IBankAccountService bankAccountService, BankAccountMapper bankAccountMapper) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMapper = bankAccountMapper;
    }

    @PostMapping
    public ResponseEntity<BankAccountDTO> create(@RequestBody BankAccountDTO dto) {
        BankAccount entity = bankAccountService.create(dto);
        BankAccountDTO responseDTO = bankAccountMapper.toDTO(entity);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> findAll() {
        List<BankAccount> entities = bankAccountService.findAll();
        List<BankAccountDTO> dtos = entities.stream()
                .map(bankAccountMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> findById(@PathVariable Integer id) {
        BankAccount entity = bankAccountService.findById(id);
        return ResponseEntity.ok(bankAccountMapper.toDTO(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccountDTO> update(@PathVariable Integer id, @RequestBody BankAccountDTO dto) {
        BankAccount updatedEntity = bankAccountService.update(id, dto);
        return ResponseEntity.ok(bankAccountMapper.toDTO(updatedEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        bankAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
