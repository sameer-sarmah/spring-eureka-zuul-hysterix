package core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferMoney {
    private Long orderId;
    private Long from;
    private Long to;
    private BigDecimal amount;
}