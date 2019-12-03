package core.commands.delivery;

import core.models.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverOrderCommand  implements  IDeliveryCommand{
    private String type = DeliverOrderCommand.class.getName();
    private long customerId;

    private long orderId;
    private long agentId;
    private Address deliveryAddress;
}
