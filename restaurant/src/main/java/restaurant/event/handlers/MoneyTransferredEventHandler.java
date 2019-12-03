package restaurant.event.handlers;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import core.events.handler.IPaymentEventHandler;
import core.events.payment.IPaymentEvent;
import core.events.payment.MoneyTransferredEvent;
import core.events.restaurant.OrderPreparationCompletedEvent;
import restaurant.entities.Order;
import restaurant.entities.OrderState;
import restaurant.respository.OrderRepository;

@Component
public class MoneyTransferredEventHandler implements IPaymentEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(MoneyTransferredEventHandler.class);

    @Value(value = "${kafka.restaurant.event.topic}")
    private String restaurantEventTopic;

    @Qualifier("kafkaTemplate")
    @Autowired
    private KafkaTemplate<Long, Object> kafkaTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean canHandle(IPaymentEvent paymentEvent) {
        return paymentEvent instanceof MoneyTransferredEvent;
    }

    @Override
    public void handle(IPaymentEvent paymentEvent) {
        if (paymentEvent instanceof MoneyTransferredEvent) {
            MoneyTransferredEvent moneyTransferredEvent = (MoneyTransferredEvent) paymentEvent;
            Optional<Order> optionalOrder = orderRepository.findById(moneyTransferredEvent.getOrderId());
            if (optionalOrder.isPresent()) {
                
            }
        }
    }


}
