package core.commands.restaurant;

import core.models.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantCommand implements IRestaurantCommand {
    private String type = CreateCustomerCommand.class.getName();
    private Restaurant restaurant;
}
