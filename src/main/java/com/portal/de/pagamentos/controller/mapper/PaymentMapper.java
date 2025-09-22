package com.portal.de.pagamentos.controller.mapper;

import com.portal.de.pagamentos.domain.payment.dto.PaymentDTO;
import com.portal.de.pagamentos.domain.payment.dto.PaymentDocumentValueDTO;
import com.portal.de.pagamentos.domain.payment.Payment;

import java.util.List;

public class PaymentMapper {

    public PaymentMapper(){
        super();
    }

    public static PaymentDTO toDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();

        // Converte o ID em String
        dto.setId(payment.getId() != null ? payment.getId().toString() : null);

        // Preenche os IBANs dos bancos (supondo que eles não sejam nulos)
        dto.setSenderBankAccountIban(payment.getSenderBankAccount().getIban().getValue());
        dto.setReceiverBankAccountIban(payment.getReceiverBankAccount().getIban().getValue());

        // Estado do pagamento
        dto.setState(payment.getState().name());

        // Informações de criação e atualização
        dto.setCreatedByUsername(payment.getCreatedBy() != null ? payment.getCreatedBy().getEmail().toString() : null);
        dto.setUpdatedByUsername(payment.getUpdatedBy() != null ? payment.getUpdatedBy().getEmail().toString() : null);

        dto.setDtCreate(payment.getDtCreated() != null ? payment.getDtCreated().toString() : null);
        dto.setDtUpdated(payment.getDtUpdated() != null ? payment.getDtUpdated().toString() : null);

        // Converte o Map<PaymentDocument, Double> para uma List de PaymentDocumentValueDTO
        List<PaymentDocumentValueDTO> docs = payment.getPaymentDocuments()
                .entrySet()
                .stream()
                .map(entry -> new PaymentDocumentValueDTO(
                        entry.getKey().getId().toString(),
                        entry.getValue()
                ))
                .toList();
        dto.setPaymentDocuments(docs);

        return dto;
    }
}

