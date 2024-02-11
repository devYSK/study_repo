package com.fastcampus.helloecommerceservice.domain.order;

import com.fastcampus.helloecommerceservice.domain.BaseEntity;
import com.fastcampus.helloecommerceservice.enums.PayType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orders", schema = "ecommerce")
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "pay_type")
    @Enumerated(value = EnumType.STRING)
    private PayType payType;
}
