package core.commands.handler;

import core.commands.delivery.IDeliveryCommand;

public interface IDeliveryCommandHandler {
    boolean canHandle(IDeliveryCommand deliveryCommand);
    void handle(IDeliveryCommand deliveryCommand);
}
