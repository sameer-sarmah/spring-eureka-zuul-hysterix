package core.deserailizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.commands.delivery.*;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class DeliveryCommandDeserializer implements Deserializer {
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
            if(type.equals(AssignAgentCommand.class.getName())){
                return mapper.readValue(data,AssignAgentCommand.class);
            }
            else if(type.equals(DeliverOrderCommand.class.getName())){
                return mapper.readValue(data, DeliverOrderCommand.class);
            }
            else if(type.equals(PickupOrderCommand.class.getName())){
                return mapper.readValue(data, PickupOrderCommand.class);
            }
            else if(type.equals(SetAgentAvailableCommand.class.getName())){
                return mapper.readValue(data, SetAgentAvailableCommand.class);
            }
            else if(type.equals(ProcessPendingDeliveryCommand.class.getName())){
                return mapper.readValue(data, ProcessPendingDeliveryCommand.class);
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
