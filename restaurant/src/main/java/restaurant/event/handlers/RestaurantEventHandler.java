package restaurant.event.handlers;

import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import core.events.delivery.IDeliveryEvent;
import core.events.handler.IDeliveryEventHandler;
import core.events.handler.IPaymentEventHandler;
import core.events.payment.IPaymentEvent;

@Component
public class RestaurantEventHandler {
    @Autowired
    List<IPaymentEventHandler> paymentEventHandlers;

    @Autowired
    List<IDeliveryEventHandler> deliveryEventHandlers;

    @KafkaListener(topics = "#{'${kafka.payment.event.topic}'}", groupId = "#{'${kafka.event.consumer.group}'}" ,
            containerFactory = "kafkaPaymentEventListenerContainerFactory")
    public void handlePaymentEvent(@Payload ConsumerRecord message, Consumer<Long,Object> consumer){
        if(message.value() != null && message.value() instanceof IPaymentEvent){
            IPaymentEvent event = (IPaymentEvent)message.value();
            for(IPaymentEventHandler commandHandler : paymentEventHandlers){
                if(commandHandler.canHandle(event)){
                    commandHandler.handle(event);
                    consumer.commitSync();
                }
            }
        }

    }


    @KafkaListener(topics = "#{'${kafka.delivery.event.topic}'}",
            groupId = "#{'${kafka.event.consumer.group}'}" , containerFactory = "kafkaDeliveryEventListenerContainerFactory")
    public void handleDeliveryEvent(@Payload ConsumerRecord message, Consumer<Long,Object> consumer){
        if(message.value() != null && message.value() instanceof IDeliveryEvent){
            IDeliveryEvent event = (IDeliveryEvent)message.value();
            for(IDeliveryEventHandler commandHandler : deliveryEventHandlers){
                if(commandHandler.canHandle(event)){
                    commandHandler.handle(event);
                    consumer.commitSync();
                }
            }
        }

    }
}
