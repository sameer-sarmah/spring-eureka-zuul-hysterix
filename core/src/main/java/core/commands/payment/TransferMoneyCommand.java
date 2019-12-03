package core.commands.payment;

import core.models.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferMoneyCommand {
    private String type = TransferMoneyCommand.class.getName();
    private Long orderId;
    private Long from;
    private Long to;
    private BigDecimal amount;
}
