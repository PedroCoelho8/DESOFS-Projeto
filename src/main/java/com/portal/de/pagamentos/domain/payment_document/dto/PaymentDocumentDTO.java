package com.portal.de.pagamentos.domain.payment_document.dto;

public class PaymentDocumentDTO {

    private String id;
    private String nifSender;
    private String nifReceiver;
    private double value;
    private String currency;
    private String createdByUsername;  // ou id, conforme o que precisa no front
    private String updatedByUsername;  // pode ser null se não atualizado
    private String dtCreate;
    private String dtUpdated;

    public PaymentDocumentDTO() {}

    public PaymentDocumentDTO(String nifSender, String nifReceiver, double value, String currency,
                              String createdByUsername, String updatedByUsername, String dtCreate, String dtUpdated) {
;
        this.nifSender = nifSender;
        this.nifReceiver = nifReceiver;
        this.value = value;
        this.currency = currency;
        this.createdByUsername = createdByUsername;
        this.updatedByUsername = updatedByUsername;
        this.dtCreate = dtCreate;
        this.dtUpdated = dtUpdated;
    }



    public String getNifSender() {
        return nifSender;
    }

    public void setNifSender(String nifSender) {
        this.nifSender = nifSender;
    }

    public String getNifReceiver() {
        return nifReceiver;
    }

    public void setNifReceiver(String nifReceiver) {
        this.nifReceiver = nifReceiver;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
