package com.ys.springbootshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : ysk
 */
@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    private List<CartOrderDto> cartOrderDtoList;
}
