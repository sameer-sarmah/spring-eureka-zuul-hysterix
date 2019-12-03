package restaurant.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@Table(name = "recipe", catalog = "restaurant",schema="restaurant")
public class Recipe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn (name="restaurant_id")
	private Restaurant restaurant;

	@Column(name="recipe_name")
	private String name;

	private String description;

	@Column(name="unit_price")
	private double price;
}
