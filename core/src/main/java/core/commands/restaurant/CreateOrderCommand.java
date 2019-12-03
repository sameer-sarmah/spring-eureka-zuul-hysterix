package core.commands.restaurant;

import core.models.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommand implements IRestaurantCommand {
    private String type = CreateOrderCommand.class.getName();
    private Order order;
    private Long toAccountId;
    private Long fromAccountId;
    private BigDecimal amount;
}
