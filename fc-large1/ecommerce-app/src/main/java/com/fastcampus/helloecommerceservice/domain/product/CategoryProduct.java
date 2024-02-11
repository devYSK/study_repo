package com.fastcampus.helloecommerceservice.domain.product;

import com.fastcampus.helloecommerceservice.domain.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "category_products", schema = "ecommerce")
@Getter
public class CategoryProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long categoryProductId;
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "is_exposed")
    private boolean isExposed;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
}
