package core.events.delivery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import core.models.Address;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPickedUpEvent implements IDeliveryEvent{
    private String type = OrderPickedUpEvent.class.getName();
    private long customerId;
    private long orderId;
    private Address deliveryAddress;
    private long agentId;
}
