package com.portal.de.pagamentos.controller.icontroller;

import com.portal.de.pagamentos.domain.payment.dto.PaymentDTO;
import com.portal.de.pagamentos.domain.payment.dto.UpdatePaymentStateDTO;

import java.util.List;

public interface IPaymentController {
    PaymentDTO createPayment(PaymentDTO dto);
    PaymentDTO sendPayment(UpdatePaymentStateDTO dto);
    PaymentDTO receivePayment(UpdatePaymentStateDTO dto);
    List<PaymentDTO> getAllPayments();
    List<PaymentDTO> getUserPayments();
}