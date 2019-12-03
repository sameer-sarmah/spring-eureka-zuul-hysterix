package core.events.restaurant;

import core.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreatedEvent implements IRestaurantEvent {
    private String type = CustomerCreatedEvent.class.getName();
    private long customerId;
    private Customer customer;
}
