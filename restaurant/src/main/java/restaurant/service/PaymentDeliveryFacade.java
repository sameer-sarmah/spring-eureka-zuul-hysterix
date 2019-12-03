package restaurant.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import core.commands.delivery.PickupOrderCommand;
import core.models.Address;
import core.models.PickupOrder;
import core.models.TransferMoney;
import reactor.core.publisher.Mono;
import restaurant.entities.Order;
import restaurant.entities.OrderState;
import restaurant.respository.OrderRepository;
import restaurant.service.api.IPaymentService;
import restaurant.util.RestaurantUtil;

@Service
public class PaymentDeliveryFacade {
	 private static final Logger logger = LoggerFactory.getLogger(PaymentDeliveryFacade.class);
	

	@Autowired
	private IPaymentService paymentService;

	@Autowired
	private OrderRepository orderRepository;
	
    @Value(value = "${kafka.payment.command.topic}")
    private String paymentCommandTopic;

    @Value(value = "${kafka.delivery.command.topic}")
    private String deliveryCommandTopic;

    @Value(value = "${kafka.restaurant.command.topic}")
    private String restaurantCommandTopic;

    @Qualifier("kafkaTemplate")
    @Autowired
    private KafkaTemplate<Long, Object> kafkaTemplate;

	public Mono<Object> validatePaymentThenPickupOrder(TransferMoney transferMoney, PickupOrder pickupOrder) {
		Mono<Object> paymentMono = paymentService.transferMoney(transferMoney);
		return Mono.create((emitter) -> {
			paymentMono.subscribe((paymentResponse) -> {
				Optional<Order> optionalOrder = orderRepository.findById(transferMoney.getOrderId());
				if (optionalOrder.isPresent()) {
					Order order = optionalOrder.get();
					order.setOrderStatus(OrderState.ORDER_ACCEPTED);
					orderRepository.saveAndFlush(order);
					logger.info(
							"Order persisted with status ORDER_ACCEPTED for order id: " + transferMoney.getOrderId());
					Long orderId = transferMoney.getOrderId();
					System.out.println(pickupOrder);
					Long customerId = pickupOrder.getCustomerId();
					Long restaurantId = pickupOrder.getRestaurantId();
					this.publishPickupOrderCommand(transferMoney.getOrderId(), pickupOrder.getCustomerId(), pickupOrder.getRestaurantId());
					emitter.success(order);
				}
			}, (Throwable exception) -> {
	            Optional<Order> optionalOrder = orderRepository.findById(transferMoney.getOrderId());
	            if (optionalOrder.isPresent()) {
	                Order order = optionalOrder.get();
	                order.setOrderStatus(OrderState.ORDER_REJECTED);
	                orderRepository.saveAndFlush(order);
	                logger.info("Order persisted with status ORDER_REJECTED for order id: "+transferMoney.getOrderId());
	            }
				emitter.error(exception);
			});
		});

	}
	
    public void publishPickupOrderCommand(long orderId,long customerId,long restaurantId) {
        PickupOrderCommand pickupOrderCommand = new PickupOrderCommand();
        Optional<Order> optionalOrder= orderRepository.findById(orderId);
        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            Address deliveryAddress = RestaurantUtil.convertAddressEntityToAddressModel(order.getAddress());
            pickupOrderCommand.setOrderId(orderId);
            pickupOrderCommand.setCustomerId(customerId);
            pickupOrderCommand.setRestaurantId(restaurantId);
            pickupOrderCommand.setDeliveryAddress(deliveryAddress);
            kafkaTemplate.send(deliveryCommandTopic,
                    pickupOrderCommand.getOrderId(), pickupOrderCommand);
            kafkaTemplate.flush();
            logger.info("PickupOrderCommand is published for order id: " +pickupOrderCommand.getOrderId());
        }

    }
}
