package core.commands.handler;

import core.commands.restaurant.IRestaurantCommand;

public interface IRestaurantCommandHandler {
    boolean canHandle(IRestaurantCommand restaurantCommand);
    void handle(IRestaurantCommand restaurantCommand);
}
