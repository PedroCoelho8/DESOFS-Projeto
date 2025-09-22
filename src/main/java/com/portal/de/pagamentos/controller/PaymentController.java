package com.portal.de.pagamentos.controller;

import com.portal.de.pagamentos.controller.icontroller.IPaymentController;
import com.portal.de.pagamentos.controller.mapper.PaymentMapper;
import com.portal.de.pagamentos.domain.payment.dto.PaymentDTO;
import com.portal.de.pagamentos.domain.payment.dto.UpdatePaymentStateDTO;
import com.portal.de.pagamentos.domain.payment.Payment;
import com.portal.de.pagamentos.service.iservices.IPaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController implements IPaymentController {

    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentDTO createPayment(@RequestBody PaymentDTO dto) {
        Payment payment = paymentService.create(dto);
        return PaymentMapper.toDTO(payment);
    }

    @PostMapping("/send")
    public PaymentDTO sendPayment(@RequestBody UpdatePaymentStateDTO dto) {
        Payment payment = paymentService.send(dto);
        return PaymentMapper.toDTO(payment);
    }

    @PostMapping("/receive")
    public PaymentDTO receivePayment(@RequestBody UpdatePaymentStateDTO dto) {
        Payment payment = paymentService.receive(dto);
        return PaymentMapper.toDTO(payment);
    }

    @GetMapping
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return payments.stream()
                .map(PaymentMapper::toDTO)
                .toList();
    }

    @GetMapping("/user")
    public List<PaymentDTO> getUserPayments() {
        List<Payment> payments = paymentService.getUserPayments();
        return payments.stream()
                .map(PaymentMapper::toDTO)
                .toList();
    }
}