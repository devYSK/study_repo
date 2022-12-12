package com.ys.rest_docs_boot2.domain.order.item;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FoodTest {

    @DisplayName("Food 생성 실패 테스트 - chef 가 빈 값이면 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createFailNickNameIsNullOrSize(String chef) {
        //given
        int price = 100000;
        int quantity = 100;

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> new Food(price, quantity, chef));
    }

    @DisplayName("Food 생성 성공 테스트")
    @Test
    void createSuccess() {
        //given
        int price = 100000;
        int quantity = 100;
        String chef = "chef";

        //when & then
        Food food = assertDoesNotThrow(() -> new Food(price, quantity, chef));

        assertEquals(chef, food.getChef());
        assertEquals(quantity, food.getStockQuantity());
        assertEquals(price, food.getPrice());
    }

}