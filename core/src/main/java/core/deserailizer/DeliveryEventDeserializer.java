package core.deserailizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.events.delivery.*;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class DeliveryEventDeserializer implements Deserializer {
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
            Object parsedObject;
            if(type.equals(OrderDeliveredEvent.class.getName())){
                return mapper.readValue(data, OrderDeliveredEvent.class);
            }
            else if(type.equals(OrderPickedUpEvent.class.getName())){
                return mapper.readValue(data, OrderPickedUpEvent.class);
            }
            else if(type.equals(AgentAssignedEvent.class.getName())){
                return mapper.readValue(data, AgentAssignedEvent.class);
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
