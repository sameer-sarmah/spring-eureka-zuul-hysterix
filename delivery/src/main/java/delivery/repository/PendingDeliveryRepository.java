package delivery.repository;

import delivery.entities.PendingDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingDeliveryRepository extends JpaRepository<PendingDelivery,Long> {
    Optional<PendingDelivery> findByOrderId(Long orderId);
}
