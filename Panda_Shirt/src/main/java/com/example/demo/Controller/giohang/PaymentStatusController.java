package com.example.demo.Controller.giohang;

import com.example.demo.entity.PaymentStatusRequest;
import com.example.demo.entity.PaymentStatusResponse;
import com.example.demo.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentStatusController {

    private final PaymentService paymentService;

    public PaymentStatusController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/check-payment-status")
    public PaymentStatusResponse checkPaymentStatus(@RequestBody PaymentStatusRequest request) {
        return paymentService.checkPaymentStatus(request.getOrderId());
    }
}

