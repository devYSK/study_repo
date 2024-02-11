package com.fastcampus.ecommerce.admin.service;

import com.fastcampus.ecommerce.admin.domain.customer.Customer;
import com.fastcampus.ecommerce.admin.domain.order.Order;
import com.fastcampus.ecommerce.admin.domain.order.OrderDetailView;
import com.fastcampus.ecommerce.admin.exception.NotFoundCustomerException;
import com.fastcampus.ecommerce.admin.repository.CustomerRepository;
import com.fastcampus.ecommerce.admin.repository.OrderRepository;
import com.fastcampus.ecommerce.admin.service.dto.OrderDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public List<OrderDetailView> findAllOrderDetailView() {
        return orderRepository.findAllOrderDetailView();
    }

    public Optional<OrderDTO> findById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return Optional.empty();
        }

        Order order = optionalOrder.get();

        Customer customer = customerRepository.findById(order.getCustomerId())
                .orElseThrow(() -> new NotFoundCustomerException("주문 고객 정보를 찾을 수 없습니다."));
        OrderDTO orderDTO = OrderDTO.of(order.getOrderId(), order.getAmount(), order.getCustomerId(), customer.getCustomerName(), order.getPayType(), order.getOrderStatus(), order.getCreatedAt());

        return Optional.of(orderDTO);
    }
}
