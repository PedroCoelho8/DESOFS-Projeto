package com.portal.de.pagamentos.domain.payment.dto;

public class PaymentDocumentValueDTO {

    private String paymentDocumentId;
    private double amount;

    public PaymentDocumentValueDTO() {}

    public PaymentDocumentValueDTO(String paymentDocumentId, double amount) {
        this.paymentDocumentId = paymentDocumentId;
        this.amount = amount;
    }

    public String getPaymentDocumentId() {
        return paymentDocumentId;
    }

    public void setPaymentDocumentId(String paymentDocumentId) {
        this.paymentDocumentId = paymentDocumentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
