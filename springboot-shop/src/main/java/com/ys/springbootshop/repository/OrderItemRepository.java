package com.ys.springbootshop.repository;

import com.ys.springbootshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : ysk
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
