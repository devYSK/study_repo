package com.fastcampus.helloecommerceservice.enums;

import lombok.Getter;

public enum DeliveryType {
    FREE("무료"),
    PAID("유료");

    @Getter
    private String description;

    DeliveryType(String description) {
        this.description = description;
    }
}
