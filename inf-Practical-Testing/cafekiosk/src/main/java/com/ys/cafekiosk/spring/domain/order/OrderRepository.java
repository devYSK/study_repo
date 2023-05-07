package com.ys.cafekiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("""
		select o from Order o where o.registeredDateTime >= :startDateTime
		and o.registeredDateTime < :endDateTime
		and o.orderStatus = :orderStatus""")
	List<Order> findOrdersBy(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus orderStatus);
}
