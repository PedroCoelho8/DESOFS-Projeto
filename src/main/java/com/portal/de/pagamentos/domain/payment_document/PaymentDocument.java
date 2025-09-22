package com.portal.de.pagamentos.domain.payment_document;

import com.portal.de.pagamentos.domain.NIF;
import com.portal.de.pagamentos.domain.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_documents")
public class PaymentDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nifSender", nullable = false))
    private NIF nifSender;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nifReceiver", nullable = false))
    private NIF nifReceiver;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private String currency;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime dtCreate;

    @Column
    private LocalDateTime dtUpdated;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;


    public PaymentDocument() {}

    public PaymentDocument(NIF nifSender, NIF nifReceiver, double value, String currency, User createdBy) {
        this.nifSender = nifSender;
        this.nifReceiver = nifReceiver;
        this.value = value;
        this.currency = currency;
        this.createdBy = createdBy;
    }

    @PreUpdate
    protected void onUpdate() {
        this.dtUpdated = LocalDateTime.now();
    }



    public UUID getId() {return id;}

    public NIF getNifSender() {return nifSender;}

    public NIF getNifReceiver() {return nifReceiver;}

    public double getValue() {return value;}

    public String getCurrency() {return currency;}

    public LocalDateTime getDtCreate() {return dtCreate;}

    public LocalDateTime getDtUpdated() {return dtUpdated;}

    public User getCreatedBy() {return createdBy;}

    public User getUpdatedBy() {return updatedBy;}


    public void setNifSender(String nif) {this.nifSender = new NIF(nif);}

    public void setNifReceiver(String nif) {this.nifReceiver = new NIF(nif);}

    public void setValue(double value) {this.value = value;}

    public void setCurrency(String currency) {this.currency = currency;}

    public void setCreatedBy(User createdBy) {this.createdBy = createdBy;}

    public void setUpdatedBy(User updatedBy) {this.updatedBy = updatedBy;}


}
