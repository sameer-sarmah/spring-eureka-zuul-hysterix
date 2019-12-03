package core.events.restaurant;
import core.events.payment.AccountCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelledEvent implements IRestaurantEvent {
    private String type = OrderCancelledEvent.class.getName();
    private long orderId;
}

