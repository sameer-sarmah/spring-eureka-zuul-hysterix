package core.events.restaurant;

import core.models.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCreatedEvent implements IRestaurantEvent {
    private String type = RestaurantCreatedEvent.class.getName();
    private Long restaurantId;
    private Restaurant restaurant;
}
