package com.ys.rest_docs_boot2.domain.order;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {

    @DisplayName("오더 생성 테스트 - memo가 빈 값이면 default 메시지로 생성된다")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void create(String memo) {
        //given & when
        Order order = Order.create(memo);
        // then
        assertEquals("부재시 전화주세요.", order.getMemo());
    }

    @DisplayName("오더 생성 테스트 - default 메시지로 생성된다")
    @Test
    void createDefaultMessage() {
        //given & when
        Order order = Order.createDefaultMessage();
        // then
        assertEquals("부재시 전화주세요.", order.getMemo());
    }


}