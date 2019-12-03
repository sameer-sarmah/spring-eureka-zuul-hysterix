package restaurant.service.impl;

import core.exception.CoreException;
import core.models.PickupOrder;
import core.models.TransferMoney;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import restaurant.entities.*;
import restaurant.respository.CustomerRepository;
import restaurant.respository.OrderRepository;
import restaurant.respository.RestaurantRepository;
import restaurant.service.PaymentDeliveryFacade;
import restaurant.service.api.IRestaurantService;
import restaurant.util.RestaurantUtil;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService implements IRestaurantService {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    @Qualifier("entityManagerFactory")
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    PaymentDeliveryFacade paymentDeliveryFacade;

    @Transactional
    public core.models.Order createOrder(core.models.Order order) throws CoreException {
        Optional<Customer> customerOptional = customerRepository.findById(order.getCustomerId());
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(order.getRestaurantId());
        if (customerOptional.isPresent() && restaurantOptional.isPresent()) {
            Customer customer = customerOptional.get();
            Restaurant restaurant = restaurantOptional.get();
            Order orderEntity = new Order();
            orderEntity.setAddress(RestaurantUtil.convertAddressModelToAddressEntity(order.getAddress()));
            orderEntity.setCustomer(customer);
            orderEntity.setRestaurant(restaurant);
            Order orderPersisted = orderRepository.save(orderEntity);
            List<Long> recipeIds = order
                    .getOrderItems()
                    .stream()
                    .map((orderItem) -> orderItem.getRecipe().getRecipeId())
                    .collect(Collectors.toList());
            TypedQuery<Recipe> query = entityManagerFactory
                    .createEntityManager()
                    .createQuery("select rec from Restaurant res JOIN res.recipes rec WHERE rec.id IN :recipes", Recipe.class);
            query.setParameter("recipes", recipeIds);
            List<Recipe> recipes = query.getResultList();
            Map<Long, Recipe> recipeMap = new HashMap<>();
            recipes.stream().forEach((recipe) -> {
                recipeMap.put(recipe.getId(), recipe);
            });

            List<OrderItem> orderItems = order
                    .getOrderItems()
                    .stream()
                    .map((orderItem) -> {
                        OrderItem orderItemEntity = RestaurantUtil.convertOrderItemModelToOrderItemEntity(orderItem, orderPersisted);
                        Recipe recipe = recipeMap.get(orderItem.getRecipe().getRecipeId());
                        orderItemEntity.setRecipe(recipe);
                        return orderItemEntity;
                    })
                    .collect(Collectors.toList());

            orderItems.stream().forEach(orderItem -> orderItem.setOrder(orderPersisted));
            orderPersisted.setOrderItems(orderItems);
            orderPersisted.setOrderStatus(OrderState.ORDER_CREATED);
            orderRepository.save(orderPersisted);
            orderRepository.flush();
            order.setOrderId(orderPersisted.getId());
            return order;
        }
        throw new CoreException("Order could not be created");
    }

    public void validatePaymentThenPickupOrder(TransferMoney transferMoney, PickupOrder pickupOrder){
        Mono<Object> responseMono = paymentDeliveryFacade.validatePaymentThenPickupOrder(transferMoney,pickupOrder);
        responseMono.subscribe((response)->{
        	logger.info("payment validated and PickupOrderCommand published");
        },(exception)->{});
    }
}
