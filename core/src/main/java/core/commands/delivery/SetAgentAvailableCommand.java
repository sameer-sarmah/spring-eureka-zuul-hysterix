package core.commands.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetAgentAvailableCommand  implements  IDeliveryCommand{
    private String type = SetAgentAvailableCommand.class.getName();
    private long agentId;
}
