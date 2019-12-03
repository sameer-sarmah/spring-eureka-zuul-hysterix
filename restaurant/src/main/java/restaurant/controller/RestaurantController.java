package restaurant.controller;

import core.exception.CoreException;
import core.models.Order;
import core.models.OrderAggregate;
import core.models.Recipe;
import core.models.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import restaurant.respository.RestaurantRepository;
import restaurant.service.PaymentDeliveryFacade;
import restaurant.service.api.IDeliveryService;
import restaurant.service.api.IPaymentService;
import restaurant.service.api.IRestaurantService;
import restaurant.util.RestaurantUtil;

import java.util.Optional;

@RestController
public class RestaurantController {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    IRestaurantService restaurantService;

    @Autowired
    IPaymentService paymentService;

    @Autowired
    IDeliveryService deliveryService;



    @PostMapping(value = "restaurant",consumes = "application/json")
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        restaurant.entities.Restaurant restaurantEntity = RestaurantUtil.convertRestaurantModelToRestaurantEntity(restaurant);
        restaurantRepository.saveAndFlush(restaurantEntity);
        return restaurant;
    }

    @PutMapping(value = "restaurant/{restaurantId}",consumes = "application/json")
    public Recipe addRecipeToRestaurant(@RequestBody  Recipe recipe, @PathVariable Long restaurantId) throws CoreException {
        Optional<restaurant.entities.Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if(restaurantOptional.isPresent()){
            restaurant.entities.Restaurant restaurant = restaurantOptional.get();
            restaurant.entities.Recipe recipeEntity =  RestaurantUtil.convertRecipeModelToRecipeEntity(recipe);
            recipeEntity.setRestaurant(restaurant);
            restaurant.getRecipes().add(recipeEntity);
            restaurantRepository.saveAndFlush(restaurant);
            return recipe;
        }
        throw new CoreException("Recipe could not be added");
    }

    @PostMapping(value = "order",consumes = "application/json")
    public void createOrder(@RequestBody OrderAggregate orderAggregate) throws CoreException {
        Order order = restaurantService.createOrder(orderAggregate.getOrder());
        orderAggregate.getTransferMoney().setOrderId(order.getOrderId());
        restaurantService.validatePaymentThenPickupOrder(orderAggregate.getTransferMoney(),orderAggregate.getPickupOrder());
    }



}
