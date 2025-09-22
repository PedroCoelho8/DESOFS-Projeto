package com.portal.de.pagamentos.service.iservices;

import com.portal.de.pagamentos.domain.payment.dto.PaymentDTO;
import com.portal.de.pagamentos.domain.payment.dto.UpdatePaymentStateDTO;
import com.portal.de.pagamentos.domain.payment.Payment;

import java.util.List;

public interface IPaymentService {

    Payment create(PaymentDTO dto);

    Payment send(UpdatePaymentStateDTO dto);

    Payment receive(UpdatePaymentStateDTO dto);

    List<Payment> getAllPayments();

    List<Payment> getUserPayments();
}
