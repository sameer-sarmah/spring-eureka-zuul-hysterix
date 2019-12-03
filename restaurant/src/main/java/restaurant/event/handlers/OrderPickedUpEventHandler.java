package restaurant.event.handlers;

import core.events.delivery.IDeliveryEvent;
import core.events.delivery.OrderPickedUpEvent;
import core.events.handler.IDeliveryEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import restaurant.entities.Order;
import restaurant.entities.OrderState;
import restaurant.respository.OrderRepository;

import java.util.Optional;

@Component
public class OrderPickedUpEventHandler implements IDeliveryEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrderPickedUpEventHandler.class);

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean canHandle(IDeliveryEvent deliveryEvent) {
        return deliveryEvent instanceof OrderPickedUpEvent;
    }

    @Override
    public void handle(IDeliveryEvent deliveryEvent) {
        if (deliveryEvent instanceof OrderPickedUpEvent) {
            OrderPickedUpEvent orderPickedUpEvent = (OrderPickedUpEvent)deliveryEvent;
            Optional<Order> optionalOrder= orderRepository.findById(orderPickedUpEvent.getOrderId());
            if(optionalOrder.isPresent()){
                Order order = optionalOrder.get();
                order.setOrderStatus(OrderState.ORDER_PICKED_UP);
                orderRepository.saveAndFlush(order);
                logger.info("Order persisted with status ORDER_PICKED_UP for order id: "+orderPickedUpEvent.getOrderId());

            }

        }
    }


}
