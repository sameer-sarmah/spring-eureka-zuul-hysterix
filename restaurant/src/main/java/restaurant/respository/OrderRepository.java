package restaurant.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restaurant.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}
