package com.portal.de.pagamentos.domain.payment;

import com.portal.de.pagamentos.domain.payment_document.PaymentDocument;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.domain.bank_account.BankAccount;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Rever
    @ElementCollection
    @CollectionTable(name = "payment_payment_documents", joinColumns = @JoinColumn(name = "payment_id"))
    @MapKeyJoinColumn(name = "payment_document_id")
    @Column(name = "amount")
    private Map<PaymentDocument, Double> paymentDocuments = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentState state;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_bank_account_id", nullable = false)
    private BankAccount senderBankAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_bank_account_id", nullable = false)
    private BankAccount receiverBankAccount;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime dtCreated;

    @Column
    private LocalDateTime dtUpdated;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    // Constructor intentionally left empty due to framework usage (e.g., JPA/Hibernate).
    // Validation and business logic are handled elsewhere (e.g., via setters or service layer),
    // so there's no need to initialize anything here.
    public Payment() {}



    @PreUpdate
    protected void onUpdate() {
        this.dtUpdated = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public Map<PaymentDocument, Double> getPaymentDocuments() { return paymentDocuments; }
    public void setPaymentDocuments(Map<PaymentDocument, Double> paymentDocuments) { this.paymentDocuments = paymentDocuments; }
    public PaymentState getState() { return state; }
    public void setState(PaymentState state) { this.state = state; }
    public BankAccount getSenderBankAccount() { return senderBankAccount; }
    public void setSenderBankAccount(BankAccount senderBankAccount) { this.senderBankAccount = senderBankAccount; }
    public BankAccount getReceiverBankAccount() { return receiverBankAccount; }
    public void setReceiverBankAccount(BankAccount receiverBankAccount) { this.receiverBankAccount = receiverBankAccount; }
    public LocalDateTime getDtCreated() { return dtCreated; }
    public LocalDateTime getDtUpdated() { return dtUpdated; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public User getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(User updatedBy) { this.updatedBy = updatedBy; }
}
