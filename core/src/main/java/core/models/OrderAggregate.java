package core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAggregate {
   private TransferMoney transferMoney;
   private PickupOrder pickupOrder;
   private Order order;
}
