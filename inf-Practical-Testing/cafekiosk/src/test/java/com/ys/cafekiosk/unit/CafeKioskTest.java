package com.ys.cafekiosk.unit;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ys.cafekiosk.unit.beverage.Americano;
import com.ys.cafekiosk.unit.beverage.Latte;
import com.ys.cafekiosk.unit.order.Order;

class CafeKioskTest {

	@Test
	void add_manual_test() {

		CafeKiosk cafeKiosk = new CafeKiosk();

		cafeKiosk.add(new Americano());

		System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
		System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
	}

	@DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
	@Test
	void add() {
		CafeKiosk cafeKiosk = new CafeKiosk();

		cafeKiosk.add(new Americano());

		assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
		assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	void addSeveralBeverages() {
		CafeKiosk cafeKiosk = new CafeKiosk();

		Americano americano = new Americano();
		cafeKiosk.add(americano, 2);

		assertThat(cafeKiosk.getBeverages().size()).isEqualTo(2);
		assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
		assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
	}

	@Test
	void addZeroBeverages() {
		CafeKiosk cafeKiosk = new CafeKiosk();

		Americano americano = new Americano();

		assertThatThrownBy(() ->
			cafeKiosk.add(americano, 0))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
	}

	@Test
	void remove() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		cafeKiosk.add(americano);
		assertThat(cafeKiosk.getBeverages()).hasSize(1);

		cafeKiosk.remove(americano);
		assertThat(cafeKiosk.getBeverages()).isEmpty();
	}

	@Test
	void clear() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();
		cafeKiosk.add(americano);
		cafeKiosk.add(latte);

		assertThat(cafeKiosk.getBeverages()).hasSize(2);
		cafeKiosk.clear();
		assertThat(cafeKiosk.getBeverages()).isEmpty();
	}


	@Test
	void createOrder() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);

		Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 4, 23, 10, 0));

		assertThat(order.getBeverages()).hasSize(1);
		assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	void calculateTotalPrice() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();
		cafeKiosk.add(americano);
		cafeKiosk.add(latte);

		int totalPrice = cafeKiosk.calculateTotalPrice();

		assertThat(totalPrice).isEqualTo(8500);
	}
}