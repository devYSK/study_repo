package com.fastcampus.helloecommerceservice.domain.product;

import com.fastcampus.helloecommerceservice.enums.DeliveryType;
import com.fastcampus.helloecommerceservice.enums.ProductStatus;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "products", schema = "ecommerce")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String productName;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_detail_url")
    private String imageDetailUrl;
    @Column(name = "product_desc")
    private String productDesc;
    @Column(name = "delivery_type")
    @Enumerated(value = EnumType.STRING)
    private DeliveryType deliveryType;
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