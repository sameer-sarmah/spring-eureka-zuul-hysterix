package core.deserailizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.commands.delivery.*;
import core.commands.restaurant.CreateCustomerCommand;
import core.commands.restaurant.CreateOrderCommand;
import core.commands.restaurant.CreateRestaurantCommand;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RestaurantCommandDeserializer implements Deserializer {
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
            if(type.equals(CreateCustomerCommand.class.getName())){
                return mapper.readValue(data,CreateCustomerCommand.class);
            }
            else if(type.equals(CreateOrderCommand.class.getName())){
                return mapper.readValue(data, CreateOrderCommand.class);
            }
            else if(type.equals(CreateRestaurantCommand.class.getName())){
                return mapper.readValue(data, CreateRestaurantCommand.class);
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
