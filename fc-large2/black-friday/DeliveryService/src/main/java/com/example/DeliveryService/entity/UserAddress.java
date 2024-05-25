package com.example.DeliveryService.entity;

import jakarta.persistence.*;

@Entity
@Table(indexes = {@Index(name = "idx_userId", columnList = "userId")})
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long userId;
    public String address;
    public String alias;

    public UserAddress() {
    }

    public UserAddress(Long userId, String address, String alias) {
        this.userId = userId;
        this.address = address;
        this.alias = alias;
    }
}
