package com.gara.service;

import com.gara.entity.Payment;
import java.util.List;

public interface PaymentService {

    List<Payment> getAll();

    void createPayment(Payment payment);

    void preAuth(Long paymentId);

    void authorizePayment(Long paymentId);

    void approvePayment(Long paymentId);

}
