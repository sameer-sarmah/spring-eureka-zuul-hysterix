package restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import restaurant.entities.Recipe;
import restaurant.entities.Restaurant;
import restaurant.respository.CustomerRepository;
import restaurant.respository.OrderRepository;
import restaurant.respository.RestaurantRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PopulateRecipe {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    public void populateRecipe(){
        Restaurant restaurant = restaurantRepository.findByName("Moriz Restaurant");
        if(restaurant != null){
            List<Recipe> recipes = new ArrayList<>();
            Recipe recipe = new Recipe();
            recipe.setPrice(50);
            recipe.setName("Grilled Chicken");
            recipe.setDescription("Grilled Chicken");
            recipe.setRestaurant(restaurant);
            recipes.add(recipe);
            restaurant.setRecipes(recipes);
            restaurantRepository.saveAndFlush(restaurant);
        }
    }
}
