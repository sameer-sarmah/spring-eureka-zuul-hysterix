package core.events.restaurant;
import core.events.payment.AccountCreatedEvent;
import core.models.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import core.models.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent implements IRestaurantEvent{
    private String type = OrderCreatedEvent.class.getName();
    private Order order;
    private long orderId;
}
