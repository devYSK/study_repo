package com.fastcampus.helloecommerceservice.controller.dto.order;

import com.fastcampus.helloecommerceservice.enums.PayType;
import lombok.Data;

@Data
public class DirectOrderRequestDTO {
    private Long productId;
    private PayType payType;
}
