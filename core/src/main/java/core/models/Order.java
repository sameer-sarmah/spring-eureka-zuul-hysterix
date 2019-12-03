package core.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {

	private Long orderId;
	
	private Long customerId;

	private Address address;

	private List<OrderItem> orderItems;

	private Long restaurantId;
}
