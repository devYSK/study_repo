package com.fastcamedu.hellographql;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products", schema = "ecommerce")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productId;
    @Column(name = "name")
    private String productName;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "vendor_id")
    private Long vendorId;
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "is_exposed")
    private boolean isExposed;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
}
