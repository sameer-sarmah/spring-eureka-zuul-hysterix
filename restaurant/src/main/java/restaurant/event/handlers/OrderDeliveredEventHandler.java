package restaurant.event.handlers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import core.events.delivery.IDeliveryEvent;
import core.events.delivery.OrderDeliveredEvent;
import core.events.handler.IDeliveryEventHandler;
import restaurant.entities.Order;
import restaurant.entities.OrderState;
import restaurant.respository.OrderRepository;

@Component
public class OrderDeliveredEventHandler implements IDeliveryEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrderDeliveredEventHandler.class);

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean canHandle(IDeliveryEvent deliveryEvent) {
        return deliveryEvent instanceof OrderDeliveredEvent;
    }

    @Override
    public void handle(IDeliveryEvent deliveryEvent) {
        if(deliveryEvent instanceof OrderDeliveredEvent){
            OrderDeliveredEvent orderDeliveredEvent = (OrderDeliveredEvent)deliveryEvent;
            Optional<Order> optionalOrder= orderRepository.findById(orderDeliveredEvent.getOrderId());
            if(optionalOrder.isPresent()){
                Order order = optionalOrder.get();
                order.setOrderStatus(OrderState.ORDER_DELIVERED);
                orderRepository.saveAndFlush(order);
                logger.info("Order persisted with status ORDER_DELIVERED for order id: "+orderDeliveredEvent.getOrderId());


            }
        }
    }


}
