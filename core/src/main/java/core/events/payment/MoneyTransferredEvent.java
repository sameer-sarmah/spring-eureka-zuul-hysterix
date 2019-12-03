package core.events.payment;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferredEvent {
    private String type = MoneyTransferredEvent.class.getName();
    private Long orderId;
    private Long from;
    private Long to;
    private BigDecimal amount;
}
