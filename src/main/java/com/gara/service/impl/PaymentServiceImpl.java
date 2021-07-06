package com.gara.service.impl;

import com.gara.entity.Payment;
import com.gara.enums.PaymentEvent;
import com.gara.enums.PaymentState;
import com.gara.repository.PaymentRepository;
import com.gara.service.PaymentService;
import com.gara.statemachine.StateMachineBuilder;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final StateMachineBuilder<PaymentState, PaymentEvent> stateMachineBuilder;
    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Transactional
    @Override
    public void createPayment(Payment payment) {
        payment.setActionString("new_payment");
        paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public void preAuth(Long paymentId) {
        stateMachineBuilder.sendEvent(paymentId, PaymentEvent.PRE_AUTHORIZE);
    }

    @Transactional
    @Override
    public void authorizePayment(Long paymentId) {
        stateMachineBuilder.sendEvent(paymentId, PaymentEvent.AUTHORIZE);
    }

    @Override
    public void approvePayment(Long paymentId) {
        stateMachineBuilder.sendEvent(paymentId, PaymentEvent.APPROVE);
    }

}
