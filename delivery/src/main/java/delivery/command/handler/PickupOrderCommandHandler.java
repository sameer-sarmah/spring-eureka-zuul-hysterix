package delivery.command.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import core.commands.delivery.IDeliveryCommand;
import core.commands.delivery.PickupOrderCommand;
import core.commands.handler.IDeliveryCommandHandler;
import core.models.PickupOrder;
import delivery.entities.Delivery;
import delivery.service.DeliveryService;

@Component
public class PickupOrderCommandHandler implements IDeliveryCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(PickupOrderCommandHandler.class);

    @Autowired
    private DeliveryService deliveryService;

    @Override
    public boolean canHandle(IDeliveryCommand deliveryCommand) {
        return deliveryCommand instanceof PickupOrderCommand;
    }

    @Override
    public void handle(IDeliveryCommand deliveryCommand) {
        if(deliveryCommand instanceof PickupOrderCommand){
            PickupOrderCommand pickupOrderCommand = (PickupOrderCommand)deliveryCommand;
            Delivery delivery = deliveryService.createDelivery(populatePickupOrder(pickupOrderCommand));
            deliveryService.assignAgent(delivery);
        }
    }


    private PickupOrder populatePickupOrder(PickupOrderCommand pickupOrderCommand){
    	PickupOrder pickupOrder = new PickupOrder();
    	pickupOrder.setCustomerId(pickupOrderCommand.getCustomerId());
    	pickupOrder.setOrderId(pickupOrderCommand.getOrderId());
    	pickupOrder.setRestaurantId(pickupOrderCommand.getRestaurantId());
    	pickupOrder.setDeliveryAddress(pickupOrderCommand.getDeliveryAddress());
        return pickupOrder;
    }
}
