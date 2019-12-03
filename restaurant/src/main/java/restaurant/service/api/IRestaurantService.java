package restaurant.service.api;

import core.exception.CoreException;
import core.models.Order;
import core.models.PickupOrder;
import core.models.TransferMoney;

public interface IRestaurantService {
    public Order createOrder(Order order) throws CoreException;
    public void validatePaymentThenPickupOrder(TransferMoney transferMoney, PickupOrder pickupOrder);
}
