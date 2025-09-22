package com.portal.de.pagamentos.domain.bank_account;

import com.portal.de.pagamentos.domain.NIF;
import com.portal.de.pagamentos.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private IBAN iban;

    @Embedded
    private SwiftBic swiftBic;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nifUser", nullable = false))
    private NIF nifUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dtCreated;

    @Column
    private LocalDateTime dtUpdated;

    public BankAccount() {}

    public BankAccount(IBAN iban, SwiftBic swiftBic, NIF nifUser, AccountType accountType, User createdBy) {
        this.iban = iban;
        this.swiftBic = swiftBic;
        this.nifUser = nifUser;
        this.accountType = accountType;
        this.createdBy = createdBy;
    }

    @PrePersist
    protected void onCreate() {
        this.dtCreated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dtUpdated = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public IBAN getIban() {
        return iban;
    }

    public void setIban(IBAN iban) {
        this.iban = iban;
    }

    public SwiftBic getSwiftBic() {
        return swiftBic;
    }

    public void setSwiftBic(SwiftBic swiftBic) {
        this.swiftBic = swiftBic;
    }

    public NIF getNifUser() {
        return nifUser;
    }

    public void setNifUser(NIF nifUser) {
        this.nifUser = nifUser;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getDtCreated() {
        return dtCreated;
    }

    public LocalDateTime getDtUpdated() {
        return dtUpdated;
    }
}
