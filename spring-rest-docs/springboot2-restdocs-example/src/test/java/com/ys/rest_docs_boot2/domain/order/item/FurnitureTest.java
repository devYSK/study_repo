package com.ys.rest_docs_boot2.domain.order.item;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FurnitureTest {

    @DisplayName("Furniture 생성 실패 테스트 - width 가 1보다 작으면 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4, -5, -6})
    void createFailWidthMin(int width) {
        //given
        int height = 20;
        int price = 100000;
        int quantity = 100;

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> new Furniture(price, quantity, width, height));
    }

    @DisplayName("Furniture 생성 실패 테스트 - height 가 1보다 작으면 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4, -5, -6})
    void createFailHeightMin(int height) {
        //given
        int width = 20;
        int price = 100000;
        int quantity = 100;

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> new Furniture(price, quantity, width, height));
    }

    @DisplayName("Furniture 생성 성공 테스트")
    @Test
    void createSuccess() {
        //given
        int width = 20;
        int price = 100000;
        int quantity = 100;
        int height = 50;

        //when & then
        Furniture furniture = assertDoesNotThrow(() -> new Furniture(price, quantity, width, height));

        assertEquals(width, furniture.getWidth());
        assertEquals(height, furniture.getHeight());
        assertEquals(quantity, furniture.getStockQuantity());
        assertEquals(price, furniture.getPrice());
    }

}