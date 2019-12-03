package core.commands.payment;

import core.events.payment.AccountCreatedEvent;
import core.models.Money;
import core.models.PaymentAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountCommand implements IPaymentCommand {
    private String type = CreateAccountCommand.class.getName();
    private BigDecimal balance;
    private PaymentAccount paymentAccount;
}
