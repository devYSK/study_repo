package com.ys.mybatistojpa.entity;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "OrderItems")
public class OrderItemEntity {
	// 복합키 필드
	@EmbeddedId
	private Pk pk = new Pk();

	@Column
	private Integer quantity;

	// OrderItemEntity-ItemEntity 연관관계 추가
	@JoinColumn(name = "item_id")
	@ManyToOne
	private ItemEntity item;

	// OrderItemEntity-OrderEntity 연관관계 추가
	@JoinColumn(name = "order_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("orderId")
	private OrderEntity order;

	// 복합키 식별자 클래스
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	@Embeddable
	public static class Pk implements Serializable {
		@Column(name = "order_id")
		private Long orderId;

		@Column(name = "line_number")
		private Integer lineNumber;

	}

}