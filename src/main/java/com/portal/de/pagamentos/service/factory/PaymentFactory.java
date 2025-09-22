package com.portal.de.pagamentos.service.factory;

import com.portal.de.pagamentos.domain.payment.dto.PaymentDTO;
import com.portal.de.pagamentos.domain.payment.dto.PaymentDocumentValueDTO;
import com.portal.de.pagamentos.domain.payment.Payment;
import com.portal.de.pagamentos.domain.payment.PaymentState;
import com.portal.de.pagamentos.domain.payment_document.PaymentDocument;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.domain.bank_account.BankAccount;

import java.util.HashMap;
import java.util.Map;

public class PaymentFactory {

    public static Payment createPayment(PaymentDTO dto, Map<String, PaymentDocument> documentMap,
                                        BankAccount sender, BankAccount receiver, User creator) {
        Payment payment = new Payment();

        Map<PaymentDocument, Double> docs = new HashMap<>();
        if (dto.getPaymentDocuments() != null) {
            for (PaymentDocumentValueDTO docValue : dto.getPaymentDocuments()) {
                PaymentDocument doc = documentMap.get(docValue.getPaymentDocumentId());
                if (doc != null) {
                    docs.put(doc, docValue.getAmount());
                }
            }
        }

        payment.setPaymentDocuments(docs);
        payment.setSenderBankAccount(sender);
        payment.setReceiverBankAccount(receiver);
        payment.setState(PaymentState.getByCode(dto.getState()));
        payment.setCreatedBy(creator);

        return payment;
    }
}
