package com.fastcampus.helloecommerceservice.domain.order;

import com.fastcampus.helloecommerceservice.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items", schema = "ecommerce")
@Getter
@Setter
public class OrderItem extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "quantity")
    private int quantity = 0;
    @Column(name = "price")
    private BigDecimal price = BigDecimal.ZERO;
}
