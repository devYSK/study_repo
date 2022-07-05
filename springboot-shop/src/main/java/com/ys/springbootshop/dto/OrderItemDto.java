package com.ys.springbootshop.dto;

import com.ys.springbootshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : ysk
 */
@Getter @Setter
public class OrderItemDto {

    private String itemNm;

    private int count;

    private int orderPrice;

    private String imgUrl;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
