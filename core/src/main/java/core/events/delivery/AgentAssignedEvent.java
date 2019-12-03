package core.events.delivery;


import core.models.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentAssignedEvent implements IDeliveryEvent {
    private String type = AgentAssignedEvent.class.getName();
    private long customerId;
    private long restaurantId;
    private long orderId;
    private long agentId;
    private Address deliveryAddress;
}
