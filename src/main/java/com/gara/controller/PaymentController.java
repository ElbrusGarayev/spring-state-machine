package com.gara.controller;

import com.gara.entity.Payment;
import com.gara.service.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public List<Payment> getPayments() {
        return paymentService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPayment(@RequestBody Payment payment) {
        paymentService.createPayment(payment);
    }

    @PutMapping("pre-auth/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void preAuthorize(@PathVariable Long id) {
        paymentService.preAuth(id);
    }

    @PutMapping("auth/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authorize(@PathVariable Long id) {
        paymentService.authorizePayment(id);
    }

    @PutMapping("approving/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approve(@PathVariable Long id) {
        paymentService.approvePayment(id);
    }

}
