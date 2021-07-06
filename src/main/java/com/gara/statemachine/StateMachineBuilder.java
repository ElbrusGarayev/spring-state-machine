package com.gara.statemachine;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;

public interface StateMachineBuilder<S, E> extends StateMachineEventAdapter<S, E> {

    void sendEvent(Long id, E event);

    void sendEventWithData(Long id, E event, Object key, Object value);

    default StateMachine<S, E> build(
            Long id,
            StateMachineFactory<S, E> stateMachineFactory,
            StateMachineInterceptorAdapter<S, E> interceptorAdapter,
            S currentStatus
    ) {
        StateMachine<S, E> sm =
                stateMachineFactory.getStateMachine(Long.toString(id));

        sm.stop();
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(interceptorAdapter);
                    sma.resetStateMachine(
                            new DefaultStateMachineContext<>(currentStatus, null, null, null));
                });

        sm.start();

        return sm;
    }

}
