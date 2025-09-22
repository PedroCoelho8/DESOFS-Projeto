package com.portal.de.pagamentos.repositories.irepositories;

import com.portal.de.pagamentos.domain.payment_document.PaymentDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IPaymentDocumentRepository extends JpaRepository<PaymentDocument, UUID> {
}
