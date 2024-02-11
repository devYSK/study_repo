package com.fastcampus.helloecommerceservice.controller.dto.order;

import com.fastcampus.helloecommerceservice.enums.PayType;
import lombok.Data;

@Data
public class OrderRequestDTO {
    private PayType payType;
}
