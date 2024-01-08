package com.ys.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	public Coupon(final Long userId) {
		this.userId = userId;
	}

}
