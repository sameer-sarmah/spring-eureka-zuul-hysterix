package restaurant.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "order_items", catalog = "restaurant",schema="restaurant")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne()
	@JoinColumn(name="order_id")
	private Order order;
	
	@OneToOne
	@JoinColumn(name="recipe_id")
	private Recipe recipe;
	
	private double quantity;
	@Column(name="unit_price")
	private double price;
	private double discount;
}
