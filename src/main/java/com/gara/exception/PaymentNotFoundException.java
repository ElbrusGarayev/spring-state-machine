package com.gara.exception;

public class PaymentNotFoundException extends NotFoundException {

    public PaymentNotFoundException(String paymentId) {
        super(String.format("Payment not found by id %s", paymentId));
    }

}
