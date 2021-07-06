package com.gara.config;

import com.gara.enums.PaymentEvent;
import com.gara.enums.PaymentState;
import java.util.EnumSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Slf4j
@EnableStateMachineFactory
@Configuration
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    private final Action<PaymentState, PaymentEvent> preAuthAction;
    private final Action<PaymentState, PaymentEvent> authAction;
    private final Action<PaymentState, PaymentEvent> approvingAction;

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates()
                .initial(PaymentState.NEW)
                .states(EnumSet.allOf(PaymentState.class))
                .end(PaymentState.APPROVED)
                .end(PaymentState.PRE_AUTHORIZATION_ERROR)
                .end(PaymentState.AUTHORIZATION_ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions.withExternal()
                .source(PaymentState.NEW)
                .target(PaymentState.PRE_AUTHORIZATION)
                .event(PaymentEvent.PRE_AUTHORIZE)
                .action(preAuthAction)
                .and()
                .withExternal()
                .source(PaymentState.PRE_AUTHORIZATION)
                .target(PaymentState.AUTHORIZATION)
                .event(PaymentEvent.AUTHORIZE)
                .action(authAction)
                .and()
                .withExternal()
                .source(PaymentState.AUTHORIZATION)
                .target(PaymentState.APPROVED)
                .event(PaymentEvent.APPROVE)
                .action(approvingAction);
    }

    @Bean
    public StateMachineListener<PaymentState, PaymentEvent> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
                log.info("State changed to " + to.getId());
            }
        };
    }

}
