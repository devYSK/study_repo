package com.ys.mybatistojpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Orders")
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long orderId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "order_date")
	private Date orderDate;

	// OrderEntity-OrderItemEntity 연관관계 추가
	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private List<OrderItemEntity> orderItems = new ArrayList<>();

}