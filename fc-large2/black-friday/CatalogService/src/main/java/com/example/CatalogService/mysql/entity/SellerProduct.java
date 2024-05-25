package com.example.CatalogService.mysql.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SellerProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long sellerId;

    public SellerProduct() {
    }

    public SellerProduct(Long sellerId) {
        this.sellerId = sellerId;
    }

}
