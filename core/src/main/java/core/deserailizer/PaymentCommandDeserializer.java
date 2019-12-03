package core.deserailizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.commands.payment.CreateAccountCommand;
import core.commands.payment.TransferMoneyCommand;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class PaymentCommandDeserializer implements Deserializer {
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
            if(type.equals(CreateAccountCommand.class.getName())){
                return mapper.readValue(data, CreateAccountCommand.class);
            }
            else if(type.equals(TransferMoneyCommand.class.getName())){
                return mapper.readValue(data, TransferMoneyCommand.class);
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
