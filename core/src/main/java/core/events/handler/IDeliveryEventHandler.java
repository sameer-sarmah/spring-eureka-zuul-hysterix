package core.events.handler;

import core.events.delivery.IDeliveryEvent;

public interface IDeliveryEventHandler {
    boolean canHandle(IDeliveryEvent deliveryEvent);
    void handle(IDeliveryEvent deliveryEvent);
}
