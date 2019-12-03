package core.events.handler;

import core.events.payment.IPaymentEvent;

public interface IPaymentEventHandler {
    boolean canHandle(IPaymentEvent paymentEvent);
    void handle(IPaymentEvent paymentEvent);
}
