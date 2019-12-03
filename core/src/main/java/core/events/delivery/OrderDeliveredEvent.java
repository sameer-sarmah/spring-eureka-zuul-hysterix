package core.events.delivery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveredEvent implements IDeliveryEvent{
    private String type = OrderDeliveredEvent.class.getName();
    private long customerId;
    private long orderId;
    private long agentId;
}
