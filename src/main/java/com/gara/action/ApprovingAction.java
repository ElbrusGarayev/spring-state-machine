package com.gara.action;

import com.gara.entity.Payment;
import com.gara.enums.PaymentEvent;
import com.gara.enums.PaymentState;
import com.gara.exception.PaymentNotFoundException;
import com.gara.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApprovingAction implements Action<PaymentState, PaymentEvent> {

    private static final String PAYMENT_ID = "PAYMENT_ID";
    private final PaymentRepository paymentRepository;

    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        Object recordId = context.getMessageHeader(PAYMENT_ID);
        Payment payment = paymentRepository.findById((Long) recordId)
                .orElseThrow(() -> new PaymentNotFoundException((String) recordId));
        payment.setActionString("payment_is_approved");
        paymentRepository.save(payment);
    }

}
