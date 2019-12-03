package core.models;

import core.exception.InsufficientBalance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Money {
    private BigDecimal amount = new BigDecimal(0);

}
