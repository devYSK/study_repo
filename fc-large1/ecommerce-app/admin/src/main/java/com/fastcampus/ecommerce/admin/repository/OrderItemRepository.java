package com.fastcampus.ecommerce.admin.repository;

import com.fastcampus.ecommerce.admin.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
