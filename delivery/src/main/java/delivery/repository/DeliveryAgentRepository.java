package delivery.repository;

import delivery.entities.DeliveryAgent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent,Long> {
    public List<DeliveryAgent> findByAvailableTrue(Pageable pageable);
}
