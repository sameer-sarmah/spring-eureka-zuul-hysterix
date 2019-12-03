package restaurant.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "order", catalog = "restaurant",schema="restaurant")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="customer_id")
	private Customer customer;

	@OneToOne
	@JoinColumn(name="restaurant_id")
	private Restaurant restaurant;

	@OneToMany(mappedBy = "order",
			cascade = CascadeType.ALL)
	private List<OrderItem> orderItems;
	
	@Embedded
	private Address address;
	
	@Enumerated(EnumType.STRING)
	private OrderState orderStatus;
	

	@Column(name="ordered_date")
	private LocalDateTime orderDate;

}
