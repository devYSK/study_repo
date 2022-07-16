package hello.itemservice.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : ysk
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
