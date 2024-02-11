package com.fastcampus.helloecommerceservice.repository.order;

import com.fastcampus.helloecommerceservice.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
