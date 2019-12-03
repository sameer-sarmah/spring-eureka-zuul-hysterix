package restaurant;

import core.commands.restaurant.CreateOrderCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import restaurant.entities.*;
import restaurant.respository.CustomerRepository;
import restaurant.respository.OrderRepository;
import restaurant.respository.RestaurantRepository;
import restaurant.util.RestaurantUtil;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PopulateOrder {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    @Qualifier("entityManagerFactory")
    private EntityManagerFactory entityManagerFactory;

    @Transactional
    public void populateOrder(CreateOrderCommand createOrderCommand) {
        Optional<Customer> customerOptional = customerRepository.findById(createOrderCommand.getOrder().getCustomerId());
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(createOrderCommand.getOrder().getRestaurantId());
        if (customerOptional.isPresent() && restaurantOptional.isPresent()) {
            Customer customer = customerOptional.get();
            Restaurant restaurant = restaurantOptional.get();
            Order order = new Order();
            order.setAddress(RestaurantUtil.convertAddressModelToAddressEntity(createOrderCommand.getOrder().getAddress()));
            order.setCustomer(customer);
            order.setRestaurant(restaurant);
            List<Long> recipeIds = createOrderCommand.getOrder()
                    .getOrderItems()
                    .stream()
                    .map((orderItem) -> orderItem.getRecipe().getRecipeId())
                    .collect(Collectors.toList());
            Order orderPersisted = orderRepository.save(order);
            TypedQuery<Recipe> query = entityManagerFactory
                    .createEntityManager()
                    .createQuery("select rec from Restaurant res JOIN res.recipes rec WHERE rec.id IN :recipes",Recipe.class);
            query.setParameter("recipes",recipeIds);
            List<Recipe> recipes = query.getResultList();
            Map<Long,Recipe> recipeMap = new HashMap<>();
            recipes.stream().forEach((recipe)->{
                recipeMap.put(recipe.getId(),recipe);
            });

            List<OrderItem> orderItems = createOrderCommand.getOrder()
                    .getOrderItems()
                    .stream()
                    .map((orderItem) -> {
                        OrderItem orderItemEntity =  RestaurantUtil.convertOrderItemModelToOrderItemEntity(orderItem, orderPersisted);
                        Recipe recipe =recipeMap.get(orderItem.getRecipe().getRecipeId());
                        orderItemEntity.setRecipe(recipe);
                        return orderItemEntity;
                    })
                    .collect(Collectors.toList());

            orderItems.stream().forEach(orderItem -> orderItem.setOrder(orderPersisted));
            orderPersisted.setOrderItems(orderItems);
            orderPersisted.setOrderStatus(OrderState.ORDER_CREATED);
            orderRepository.save(orderPersisted);
            orderRepository.flush();
        }
    }
}
