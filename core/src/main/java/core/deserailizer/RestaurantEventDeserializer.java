package core.deserailizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.commands.restaurant.CreateCustomerCommand;
import core.commands.restaurant.CreateOrderCommand;
import core.commands.restaurant.CreateRestaurantCommand;
import core.events.restaurant.*;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RestaurantEventDeserializer implements Deserializer {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        String jsonStr = new String(data);
        JsonNode parent;
        try {
            ObjectMapper mapper = new ObjectMapper();
            parent = mapper.readTree(jsonStr);
            String type = parent.get("type").asText();
            if(type.equals(CustomerCreatedEvent.class.getName())){
                return mapper.readValue(data,CustomerCreatedEvent.class);
            }
            else if(type.equals(OrderAcceptedEvent.class.getName())){
                return mapper.readValue(data, OrderAcceptedEvent.class);
            }
            else if(type.equals(OrderCancelledEvent.class.getName())){
                return mapper.readValue(data, OrderCancelledEvent.class);
            }
            else if(type.equals(OrderCreatedEvent.class.getName())){
                return mapper.readValue(data, OrderCreatedEvent.class);
            }
            else if(type.equals(OrderPreparationCompletedEvent.class.getName())){
                return mapper.readValue(data, OrderPreparationCompletedEvent.class);
            }
            else if(type.equals(OrderRejectedEvent.class.getName())){
                return mapper.readValue(data, OrderRejectedEvent.class);
            }
            else if(type.equals(RestaurantCreatedEvent.class.getName())){
                return mapper.readValue(data, RestaurantCreatedEvent.class);
            }


        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {

    }
}
