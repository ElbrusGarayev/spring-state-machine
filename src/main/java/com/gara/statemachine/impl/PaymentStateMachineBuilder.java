package com.gara.statemachine.impl;

import com.gara.config.PaymentStateChangeInterceptor;
import com.gara.enums.PaymentEvent;
import com.gara.enums.PaymentState;
import com.gara.repository.PaymentRepository;
import com.gara.statemachine.StateMachineBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("paymentStateMachineBuilder")
public class PaymentStateMachineBuilder implements StateMachineBuilder<PaymentState, PaymentEvent> {

    private final StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;
    private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;
    private final PaymentRepository paymentRepository;

    @Override
    public void sendEvent(Long paymentId, PaymentEvent paymentEvent) {
        StateMachine<PaymentState, PaymentEvent> sm = paymentRepository.findById(paymentId).map(
                payment -> build(payment.getId(), stateMachineFactory, paymentStateChangeInterceptor, payment.getStatus())
        ).orElseThrow();
        sm.sendEvent(createMessage(paymentEvent, "PAYMENT_ID", paymentId));
    }

    @Override
    public void sendEventWithData(Long id, PaymentEvent paymentEvent, Object key, Object value) {
        //not used in this stage
    }

}
