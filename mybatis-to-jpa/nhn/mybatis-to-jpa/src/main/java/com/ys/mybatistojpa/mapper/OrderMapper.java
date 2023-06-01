package com.ys.mybatistojpa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ys.mybatistojpa.model.Order;

// NOTE #14 : Orders 테이블에 대한 CRUD 쿼리에 맵핑되는 java interface 메쏘드
public interface OrderMapper {
    int getOrderCount();

    List<Order> getOrders(@Param("offset") int offset, @Param("limit") int limit);

    Order getOrder(Long orderId);

    int insertOrder(Order order);

    int deleteOrder(Long orderId);

}
