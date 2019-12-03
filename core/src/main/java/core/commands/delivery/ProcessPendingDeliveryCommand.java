package core.commands.delivery;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcessPendingDeliveryCommand implements IDeliveryCommand {
    private String type = ProcessPendingDeliveryCommand.class.getName();
}
