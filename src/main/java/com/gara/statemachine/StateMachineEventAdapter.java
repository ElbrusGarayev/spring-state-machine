package com.gara.statemachine;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;

public interface StateMachineEventAdapter<S, E> {

    default void newEvent(StateContext<S, E> context, E event, String headerKey, Object id, Object key, Object value) {
        context.getExtendedState().getVariables().put(key, value);
        context.getStateMachine()
                .sendEvent(createMessage(event, headerKey, id));
    }

    default void newEvent(StateContext<S, E> context, E event, String headerKey, Object id) {
        context
                .getStateMachine()
                .sendEvent(createMessage(event, headerKey, id));
    }

    default Message<E> createMessage(E event, String headerKey, Object id) {
        return MessageBuilder.withPayload(event)
                .setHeader(headerKey, id)
                .build();
    }

}
