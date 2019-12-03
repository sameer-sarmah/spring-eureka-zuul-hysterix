package core.events.restaurant;
import core.events.payment.AccountCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPreparationCompletedEvent implements IRestaurantEvent{
    private String type = OrderPreparationCompletedEvent.class.getName();
    private long orderId;
    private LocalDateTime completedAt;
}
