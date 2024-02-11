package com.fastcampus.helloorderapi.controller;

import com.fastcampus.helloorderapi.domain.Order;
import com.fastcampus.helloorderapi.domain.OrderCompletedMessage;
import com.fastcampus.helloorderapi.infra.kafka.OrderEventAdapter;
import com.fastcampus.helloorderapi.service.OrderQueryService;
import com.fastcampus.helloorderapi.service.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderQueryService orderQueryService;
    private final OrderEventAdapter orderEventAdapter;

    @GetMapping({"/orders/", "/orders"})
    public List<OrderDTO> index(@PageableDefault(size=20, sort="orderId",direction = Sort.Direction.ASC) Pageable pageable) {
        log.info(">>> Request orders");
        Page<Order> orders = orderQueryService.findAll(pageable);
        return orders.stream().map(order -> OrderDTO.of(order.getOrderId(), order.getAmount(), order.getCustomerId(), order.getCreatedAt())).collect(Collectors.toList());
    }

    @GetMapping({"/orders/{orderId}"})
    public Order getOrder(@PathVariable Long orderId) {
        return orderQueryService.getOrder(orderId).orElseThrow(() -> new RuntimeException());
    }

    @PostMapping({"/orders/{orderId}"})
    public Order saveOrder(@PathVariable Long orderId) {

        Optional<Order> order = orderQueryService.getOrder(orderId);

        // 샘플 주문 완료 메세지
        OrderCompletedMessage orderCompletedMessage = OrderCompletedMessage.builder()
                .txId(UUID.randomUUID().toString())
                .version("1.0")
                .completedAt(OffsetDateTime.now().toString())
                .orderId(orderId.toString())
                .customerId("100")
                .build();

        orderEventAdapter.send(orderCompletedMessage);
        log.info(">>> 주문 완료 이벤트 발행, {}", orderId);

        return order.orElse(null);
    }
}
