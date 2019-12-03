package restaurant.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restaurant.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
