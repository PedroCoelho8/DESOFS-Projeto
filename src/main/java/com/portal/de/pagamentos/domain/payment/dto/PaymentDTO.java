package com.portal.de.pagamentos.domain.payment.dto;

import java.util.List;

public class PaymentDTO {

    private String id;
    private List<PaymentDocumentValueDTO> paymentDocuments; // inclui valor associado, PaymentDocumentValueDTO
    private String senderBankAccountIban;
    private String receiverBankAccountIban;
    private String state;
    private String createdByUsername;
    private String updatedByUsername;
    private String dtCreate;
    private String dtUpdated;

    public PaymentDTO() {}

    public PaymentDTO( List<PaymentDocumentValueDTO> paymentDocuments,
                      String senderBankAccountIban, String receiverBankAccountIban,
                      String state, String createdByUsername, String updatedByUsername,
                      String dtCreate, String dtUpdated) {
        this.paymentDocuments = paymentDocuments;
        this.senderBankAccountIban = senderBankAccountIban;
        this.receiverBankAccountIban = receiverBankAccountIban;
        this.state = state;
        this.createdByUsername = createdByUsername;
        this.updatedByUsername = updatedByUsername;
        this.dtCreate = dtCreate;
        this.dtUpdated = dtUpdated;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PaymentDocumentValueDTO> getPaymentDocuments() {
        return paymentDocuments;
    }

    public void setPaymentDocuments(List<PaymentDocumentValueDTO> paymentDocuments) {
        this.paymentDocuments = paymentDocuments;
    }

    public String getSenderBankAccountIban() {
        return senderBankAccountIban;
    }

    public void setSenderBankAccountIban(String senderBankAccountIban) {
        this.senderBankAccountIban = senderBankAccountIban;
    }

    public String getReceiverBankAccountIban() {
        return receiverBankAccountIban;
    }

    public void setReceiverBankAccountIban(String receiverBankAccountIban) {
        this.receiverBankAccountIban = receiverBankAccountIban;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public String getUpdatedByUsername() {
        return updatedByUsername;
    }

    public void setUpdatedByUsername(String updatedByUsername) {
        this.updatedByUsername = updatedByUsername;
    }

    public String getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(String dtCreate) {
        this.dtCreate = dtCreate;
    }

    public String getDtUpdated() {
        return dtUpdated;
    }

    public void setDtUpdated(String dtUpdated) {
        this.dtUpdated = dtUpdated;
    }
}
