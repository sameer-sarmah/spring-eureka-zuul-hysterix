package core.events.restaurant;
import core.events.payment.AccountCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRejectedEvent implements IRestaurantEvent{
    private String type = OrderRejectedEvent.class.getName();
    private long orderId;
}
