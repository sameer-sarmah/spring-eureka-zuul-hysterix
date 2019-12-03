package delivery.command.handler;

import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import core.commands.delivery.IDeliveryCommand;
import core.commands.handler.IDeliveryCommandHandler;

@Component
public class DeliveryCommandHandler {
    @Autowired
    List<IDeliveryCommandHandler> deliveryCommandHandlers;


    @KafkaListener(topics = "#{'${kafka.command.topic}'}", groupId = "#{'${kafka.command.consumer.group}'}" , containerFactory = "kafkaCommandListenerContainerFactory")
    public void handle(@Payload ConsumerRecord message,Consumer<Long,Object> consumer){
        if(message.value() != null && message.value() instanceof IDeliveryCommand){
            IDeliveryCommand event = (IDeliveryCommand)message.value();
            for(IDeliveryCommandHandler commandHandler : deliveryCommandHandlers){
                if(commandHandler.canHandle(event)){
                    commandHandler.handle(event);
                    consumer.commitSync();
                }
            }
        }

    }
}
