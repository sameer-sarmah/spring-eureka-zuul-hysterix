package core.events.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreatedEvent implements IPaymentEvent{
    private String type = AccountCreatedEvent.class.getName();
    private long accountId;
}
