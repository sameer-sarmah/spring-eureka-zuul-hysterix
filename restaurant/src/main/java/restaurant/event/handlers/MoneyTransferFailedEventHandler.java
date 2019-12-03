package restaurant.event.handlers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import core.events.handler.IPaymentEventHandler;
import core.events.payment.IPaymentEvent;
import core.events.payment.MoneyTransferFailedEvent;
import restaurant.entities.Order;
import restaurant.entities.OrderState;
import restaurant.respository.OrderRepository;

@Component
public class MoneyTransferFailedEventHandler implements IPaymentEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(MoneyTransferFailedEventHandler.class);

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean canHandle(IPaymentEvent paymentEvent) {
        return paymentEvent instanceof MoneyTransferFailedEvent;
    }

    @Override
    public void handle(IPaymentEvent paymentEvent) {
        if(paymentEvent instanceof MoneyTransferFailedEvent) {
           
        }
    }
}
