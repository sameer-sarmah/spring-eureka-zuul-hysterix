package restaurant.util;


import restaurant.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {
   public static Address convertAddressModelToAddressEntity(core.models.Address address){
        Address entity = new Address();
        entity.setAddress(address.getAddress());
        entity.setCity(address.getCity());
        entity.setCountry(address.getCountry());
        entity.setPhone(address.getPhone());
        entity.setState(address.getState());
        entity.setZip(address.getZip());
        return entity;
    }

    public static core.models.Address convertAddressEntityToAddressModel(Address entity){
        core.models.Address address = new core.models.Address();
        address.setAddress(entity.getAddress());
        address.setCity(entity.getCity());
        address.setCountry(entity.getCountry());
        address.setPhone(entity.getPhone());
        address.setState(entity.getState());
        address.setZip(entity.getZip());
        return address;
    }

    public static core.models.Customer convertCustomerEntityToCustomerModel(Customer entity){
        core.models.Customer customer = new core.models.Customer();
        customer.setName(entity.getName());
        customer.setEmail(entity.getEmail());
        customer.setAddress(convertAddressEntityToAddressModel(entity.getAddress()));
        return customer;
    }

    public static Customer convertCustomerModelToAddressCustomer(core.models.Customer customer){
        Customer entity = new Customer();
        entity.setAddress(convertAddressModelToAddressEntity(customer.getAddress()));
        entity.setEmail(customer.getEmail());
        entity.setName(customer.getName());
        return entity;
    }

    public static core.models.Restaurant convertRestaurantEntityToRestaurantModel(Restaurant entity){
        core.models.Restaurant restaurant = new core.models.Restaurant();
        restaurant.setEmail(entity.getEmail());
        restaurant.setName(entity.getName());
        restaurant.setAddress(convertAddressEntityToAddressModel(entity.getAddress()));
        return restaurant;
    }

    public static Restaurant convertRestaurantModelToRestaurantEntity(core.models.Restaurant restaurant){
        Restaurant entity = new Restaurant();
        entity.setName(restaurant.getName());
        entity.setEmail(restaurant.getEmail());
        entity.setAddress(convertAddressModelToAddressEntity(restaurant.getAddress()));
        return entity;
    }

    public static core.models.Recipe convertRecipeEntityToRecipeModel(Recipe entity){
        core.models.Recipe recipe = new core.models.Recipe();
        recipe.setName(entity.getName());
        recipe.setDescription(entity.getDescription());
        recipe.setUnitPrice(entity.getPrice());
        return recipe;
    }

    public static Recipe convertRecipeModelToRecipeEntity(core.models.Recipe recipe){
        Recipe entity = new Recipe();
        entity.setName(recipe.getName());
        entity.setDescription(recipe.getDescription());
        entity.setPrice(recipe.getUnitPrice());
        return entity;
    }

    public static core.models.OrderItem convertOrderItemEntityToOrderItemModel(OrderItem entity){
        core.models.OrderItem orderItem = new core.models.OrderItem();
        orderItem.setOrderId(entity.getOrder().getId());
        orderItem.setQuantity(entity.getQuantity());
        orderItem.setRecipe(convertRecipeEntityToRecipeModel(entity.getRecipe()));
        return orderItem;
    }

    public static OrderItem convertOrderItemModelToOrderItemEntity(core.models.OrderItem orderItem,Order orderEntity){
        OrderItem entity = new OrderItem();
        entity.setOrder(orderEntity);
        entity.setQuantity(orderItem.getQuantity());
        entity.setRecipe(convertRecipeModelToRecipeEntity(orderItem.getRecipe()));
        entity.setPrice(orderItem.getRecipe().getUnitPrice());
        return entity;
    }

    public static core.models.Order convertOrderEntityToOrderModel(Order entity){
        core.models.Order order = new core.models.Order();
        order.setAddress(convertAddressEntityToAddressModel(entity.getAddress()));
        order.setCustomerId(entity.getCustomer().getId());
        List<core.models.OrderItem> orderItems = entity.getOrderItems()
                .stream()
                .map(orderItemEntity -> convertOrderItemEntityToOrderItemModel(orderItemEntity))
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);
        return order;
    }


}
