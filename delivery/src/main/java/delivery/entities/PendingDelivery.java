package delivery.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "pending_delivery", catalog = "delivery",schema= "delivery")
public class PendingDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "customer_id")
    private long customerId;

    @Column(name = "restaurant_id")
    private long restaurantId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Embedded
    private Address deliveryAddress;
}
