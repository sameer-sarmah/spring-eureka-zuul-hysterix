package core.models;

import core.commands.delivery.IDeliveryCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickupOrder  {
    private long customerId;
    private long restaurantId;
    private long orderId;
    private Address deliveryAddress;
}
