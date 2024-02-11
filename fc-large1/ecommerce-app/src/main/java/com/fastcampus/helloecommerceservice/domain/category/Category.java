package com.fastcampus.helloecommerceservice.domain.category;

import com.fastcampus.helloecommerceservice.domain.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "categories", schema = "ecommerce")
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long categoryId;
    @Column(name = "name")
    private String categoryName;
    @Column(name = "order")
    private int order;
    @Column(name = "is_exposed")
    private boolean isExposed;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
}
