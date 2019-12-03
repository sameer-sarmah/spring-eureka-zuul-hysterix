package restaurant;


import core.commands.restaurant.CreateOrderCommand;
import core.exception.CoreException;
import core.http.client.ReactiveHttpClient;
import core.models.Order;
import core.models.OrderAggregate;
import core.models.OrderItem;
import core.models.PickupOrder;
import core.models.Recipe;
import core.models.TransferMoney;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import restaurant.config.RestaurantKafkaConfig;
import restaurant.config.RestaurantSQLConfig;
import restaurant.entities.Address;
import restaurant.entities.Customer;
import restaurant.entities.Restaurant;
import restaurant.respository.CustomerRepository;
import restaurant.respository.RestaurantRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RestaurantApp {
    public static void main(String[] args) throws InterruptedException {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(RestaurantKafkaConfig.class, RestaurantSQLConfig.class);
        RestaurantRepository restaurantRepository = ctx.getBean(RestaurantRepository.class);
        CustomerRepository customerRepository = ctx.getBean(CustomerRepository.class);

        //customerRepository.saveAndFlush(createCustomer());
        //restaurantRepository.saveAndFlush(createRestaurant());

        //PopulateOrder populateOrder = ctx.getBean(PopulateOrder.class);
        //populateOrder.populateOrder(buildCreateOrderCommand());
        //PopulateRecipe populateRecipe = ctx.getBean(PopulateRecipe.class);
        //populateRecipe.populateRecipe();

        ReactiveHttpClient reactiveHttpClient =  ctx.getBean(ReactiveHttpClient.class);
        Scheduler schedular = ctx.getBean(Scheduler.class);
        createOrder(reactiveHttpClient,schedular);
        Thread.sleep(1000000);
    }

    
    
    private static Customer createCustomer() {
        Customer customer = new Customer();
        customer.setEmail("sam@gmail.com");
        Address address = new Address();
        address.setAddress("DSR Spring beauty Apt,Brookfield");
        address.setCity("Bangalore");
        address.setCountry("India");
        address.setPhone("1234");
        address.setState("Karnataka");
        address.setZip("560037");
        customer.setAddress(address);
        customer.setName("Sameer");
        return customer;
    }

    private static Restaurant createRestaurant() {
        Address address = new Address();
        address.setAddress("Moriz Restaurant,Brookfield");
        address.setCity("Bangalore");
        address.setCountry("India");
        address.setPhone("458878");
        address.setState("Karnataka");
        address.setZip("560037");
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setName("Moriz Restaurant");
        restaurant.setEmail("moriz.restaurant@gmail.com");
        return restaurant;
    }

    private static void createOrder(ReactiveHttpClient reactiveHttpClient,Scheduler schedular) {
    	OrderAggregate orderAggregate = new OrderAggregate();
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setUnitPrice(50);
        recipe.setDescription("Grilled chicked");
        recipe.setName("Grilled chicked");
        recipe.setRecipeId(3L);
        recipe.setRestaurantId(1L);
        recipe.setUnitPrice(50);
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        orderItem.setRecipe(recipe);
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);
        core.models.Address address = new core.models.Address();
        address.setAddress("DSR Spring beauty Apt,Brookfield");
        address.setCity("Bangalore");
        address.setCountry("India");
        address.setPhone("1234");
        address.setState("Karnataka");
        address.setZip("560037");
        order.setAddress(address);
        order.setRestaurantId(1L);
        order.setCustomerId(1L);
        
        orderAggregate.setOrder(order);
        TransferMoney transferMoney = new TransferMoney();
        transferMoney.setFrom(3L);
        transferMoney.setTo(4L);
        transferMoney.setAmount(new BigDecimal(50));
        orderAggregate.setTransferMoney(transferMoney);
        PickupOrder pickupOrder = new PickupOrder();
        pickupOrder.setCustomerId(1L);
        pickupOrder.setDeliveryAddress(address);
        pickupOrder.setRestaurantId(1L);
        orderAggregate.setPickupOrder(pickupOrder);
        ObjectMapper objectMapper = new ObjectMapper();
        
        
        
        String path = "order";
        String url = "http://localhost:8080/";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();
        Mono<ClientResponse> responseMono;
		try {
			String jsonString= objectMapper.writeValueAsString(orderAggregate);
			responseMono = reactiveHttpClient.request(WebClient.builder(),url, path, HttpMethod.POST, headers, queryParams, jsonString);
	        responseMono.subscribeOn(schedular);
	        Consumer<ClientResponse> onSuccess = (ClientResponse clientResponse) -> {
	            clientResponse.bodyToMono(String.class).subscribe((json) -> {
	                System.out.println(json);
	            });
	        };
	        Consumer<Throwable> onError = (exception) -> {
	            exception.printStackTrace();
	        };
	        responseMono.subscribe(onSuccess, onError);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }


}
