package core.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Recipe {
	private Long recipeId;
	private Long restaurantId;
	private String name;
	
	private String description;

	private double unitPrice;
}
