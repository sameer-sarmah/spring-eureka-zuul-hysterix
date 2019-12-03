package core.commands.delivery;

import core.models.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignAgentCommand implements  IDeliveryCommand{
    private String type = AssignAgentCommand.class.getName();
    private long customerId;
    private long restaurantId;
    private long orderId;
    private Address deliveryAddress;
}
