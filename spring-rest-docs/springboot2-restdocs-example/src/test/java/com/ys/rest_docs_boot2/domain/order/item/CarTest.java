package com.ys.rest_docs_boot2.domain.order.item;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CarTest {

    @DisplayName("Car 생성 실패 테스트 - power 가 0보다 작으면 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4, -5, -6})
    void createFailpowerMin(int power) {
        //given
        int price = 100000;
        int quantity = 100;

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> new Car(price, quantity, power));
    }

    @DisplayName("Car 생성 성공 테스트")
    @Test
    void createSuccess() {
        //given
        int price = 100000;
        int quantity = 100;
        int power = 100;

        //when & then
        Car car = assertDoesNotThrow(
            () -> new Car(price, quantity, power));

        assertEquals(power, car.getPower());
        assertEquals(quantity, car.getStockQuantity());
        assertEquals(price, car.getPrice());
    }

}