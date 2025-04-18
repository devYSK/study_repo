package com.ys.mybatistojpa.mapper;

import com.ys.mybatistojpa.model.OrderItem;

// NOTE #17 : OrderItems 테이블에 대한 CRUD 쿼리에 맵핑되는 java interface 메쏘드
public interface OrderItemMapper {
    int insertOrderItem(OrderItem orderItem);

    int deleteOrderItem(Long orderId);

}
