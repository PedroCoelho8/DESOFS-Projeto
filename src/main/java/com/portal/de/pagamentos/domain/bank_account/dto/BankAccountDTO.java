package com.portal.de.pagamentos.domain.bank_account.dto;

import java.time.LocalDateTime;

public class BankAccountDTO {
    private String id;
    private String iban;
    private String swiftBic;
    private String nifUser;
    private String accountType;
    private String createdById;
    private String updatedById;
    private LocalDateTime dtCreated;
    private LocalDateTime dtUpdated;

    public BankAccountDTO() {}

    public BankAccountDTO(String iban, String swiftBic, String nifUser, String accountType, String createdById, String updatedById, LocalDateTime dtCreated, LocalDateTime dtUpdated) {
        this.iban = iban;
        this.swiftBic = swiftBic;
        this.nifUser = nifUser;
        this.accountType = accountType;
        this.createdById = createdById;
        this.updatedById = updatedById;
        this.dtCreated = dtCreated;
        this.dtUpdated = dtUpdated;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getSwiftBic() {
        return swiftBic;
    }

    public void setSwiftBic(String swiftBic) {
        this.swiftBic = swiftBic;
    }

    public String getNifUser() {
        return nifUser;
    }

    public void setNifUser(String nifUser) {
        this.nifUser = nifUser;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(String updatedById) {
        this.updatedById = updatedById;
    }

    public LocalDateTime getDtCreated() {
        return dtCreated;
    }

    public void setDtCreated(LocalDateTime dtCreated) {
        this.dtCreated = dtCreated;
    }

    public LocalDateTime getDtUpdated() {
        return dtUpdated;
    }

    public void setDtUpdated(LocalDateTime dtUpdated) {
        this.dtUpdated = dtUpdated;
    }
}
