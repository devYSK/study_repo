package com.fastcampus.helloecommerceservice.enums;

public enum PayType {
    CREDIT_CARD("신용카드"),
    CASH("현금");

    private String description;

    PayType(String description) {
        this.description = description;
    }
}
