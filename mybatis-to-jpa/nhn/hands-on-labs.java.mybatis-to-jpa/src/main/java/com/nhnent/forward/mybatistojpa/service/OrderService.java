package com.nhnent.forward.mybatistojpa.service;

import com.nhnent.forward.mybatistojpa.mapper.OrderItemMapper;
import com.nhnent.forward.mybatistojpa.mapper.OrderMapper;
import com.nhnent.forward.mybatistojpa.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;


    // NOTE #23 : pagination 구현
    public List<Order> getOrders(int pageNumber, int pageSize) {
        int totalCount = orderMapper.getOrderCount();

        int pageOffset = (pageNumber - 1) * pageSize;
        if (pageOffset >= totalCount) {
            return Collections.emptyList();
        }

        return orderMapper.getOrders(pageOffset, pageSize);
    }

    // NOTE #19 : 어플리케이션에서의 사용
    public Order getOrder(Long orderId) {
        return orderMapper.getOrder(orderId);
    }

    @Transactional
    public Order createOrder(Order order) {
        int count = orderMapper.insertOrder(order);
        if (count != 1) {
            throw new RuntimeException("can't create order");
        }

        order.getOrderItems()
             .forEach(orderItem -> {
                 orderItem.setOrderId(order.getOrderId());
                 orderItemMapper.insertOrderItem(orderItem);
             });

        return order;
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        orderItemMapper.deleteOrderItem(orderId);
        orderMapper.deleteOrder(orderId);
    }

}
