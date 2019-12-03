package core.deserailizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.events.payment.AccountCreatedEvent;
import core.events.payment.MoneyTransferFailedEvent;
import core.events.payment.MoneyTransferredEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class PaymentEventDeserializer implements Deserializer {
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
            if(type.equals(MoneyTransferFailedEvent.class.getName())){
                return mapper.readValue(data,MoneyTransferFailedEvent.class);
            }
            else if(type.equals(AccountCreatedEvent.class.getName())){
                return mapper.readValue(data, AccountCreatedEvent.class);
            }
            else if(type.equals(MoneyTransferredEvent.class.getName())){
                return mapper.readValue(data, MoneyTransferredEvent.class);
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
