package restaurant.service.api;

import core.models.TransferMoney;
import reactor.core.publisher.Mono;

public interface IPaymentService {
    public Mono<Object> transferMoney(TransferMoney transferMoney);
}
