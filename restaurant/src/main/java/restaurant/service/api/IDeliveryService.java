package restaurant.service.api;

import core.models.PickupOrder;
import reactor.core.publisher.Mono;

public interface IDeliveryService {
    Mono<Void> pickupOrder(PickupOrder pickupOrder);
}
