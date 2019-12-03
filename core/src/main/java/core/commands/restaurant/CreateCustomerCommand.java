package core.commands.restaurant;
import core.commands.payment.CreateAccountCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import core.models.Customer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerCommand implements IRestaurantCommand {
    private String type = CreateCustomerCommand.class.getName();
    private Customer customer;
}
