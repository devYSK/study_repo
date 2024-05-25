package com.example.CatalogService.cassandra.entity;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table
public class Product {

    @PrimaryKey
    public Long id;

    @Column
    public Long sellerId;

    @Column
    public String name;

    @Column
    public String description;

    @Column
    public Long price;

    @Column
    public Long stockCount;

    @Column
    public List<String> tags;

    public Product(Long id, Long sellerId, String name, String description, Long price, Long stockCount, List<String> tags) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockCount = stockCount;
        this.tags = tags;
    }
}
