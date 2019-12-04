package delivery.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import core.models.PickupOrder;
import delivery.entities.Delivery;
import delivery.service.DeliveryService;

@RestController
public class DeliveryController {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

    @Autowired
    private DeliveryService deliveryService;

    @PutMapping(value = "delivery/picked/{orderId}")
    public void setDeliveryAsPicked(@PathVariable Long orderId){
        deliveryService.setDeliveryStateToPickedUp(orderId);
    }

    @PutMapping(value = "delivery/delivered/{orderId}")
    public void setDeliveryAsDelivered(@PathVariable Long orderId){
        deliveryService.setDeliveryStateToDelivered(orderId);
    }

    @PostMapping(value = "delivery/pickup-order")
    public void pickupOrder(@RequestBody PickupOrder pickupOrder){
        Delivery delivery = deliveryService.createDelivery(pickupOrder);
        deliveryService.assignAgent(delivery);
    }
    
    @GetMapping(value="delivery/{orderId}",produces = "application/json")
    public Delivery getDelivery(@PathVariable Long orderId) {
    	return deliveryService.getDelivery(orderId);
    }
    
}
