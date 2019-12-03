package core.models;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class OrderItem {

	private long orderId;

	private Recipe recipe;
	
	private double quantity;
}
