package com.ys.rest_docs_boot2.domain.customer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CustomerTest {


    @DisplayName("customer 생성 실패 테스트 - 이름이 빈 값이면 생성에 실패하고 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createFailFirstNameIsEmpty(String firstName) {
        //given
        String lastName = "kim";
        //when & then
        assertThrows(IllegalArgumentException.class, () -> new Customer(firstName, lastName));
    }

    @DisplayName("customer 생성 실패 테스트 - 이름이 null 이면 생성에 실패하고 예외를 던진다.")
    @ParameterizedTest
    @EmptySource
    void createFailFirstNameIsNull(String firstName) {
        //given
        String lastName = "kim";
        //when & then
        assertThrows(IllegalArgumentException.class, () -> new Customer(firstName, lastName));
    }


    @DisplayName("customer 생성 실패 테스트 - 성이 빈 값이면 생성에 실패하고 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createFailLastNameIsEmpty(String lastName) {
        //given
        String firstName = "kim";
        //when & then
        assertThrows(IllegalArgumentException.class, () -> new Customer(firstName, lastName));
    }

    @DisplayName("customer 생성 실패 테스트 - 성이 null 이면 생성에 실패하고 예외를 던진다.")
    @ParameterizedTest
    @EmptySource
    void createFailLastNameIsNull(String lastName) {
        //given
        String firstName = "kim";
        //when & then
        assertThrows(IllegalArgumentException.class, () -> new Customer(firstName, lastName));
    }

    @DisplayName("customer 생성 실패 테스트 - 성과 이름이 null 이면 생성에 실패하고 예외를 던진다.")
    @Test
    void createFailFirstNameAndLastNameIsNull() {
        //given
        String firstName = null;
        String lastName = null;
        //when & then
        assertThrows(IllegalArgumentException.class, () -> new Customer(firstName, lastName));
    }

    @DisplayName("customer 생성 실패 테스트 - 성과 이름이 빈 값 이면 생성에 실패하고 예외를 던진다.")
    @ParameterizedTest
    @CsvSource(delimiterString = ",", value = {",", " , "})
    void createFailFirstNameAndLastNameIsEmpty(String firstName, String lastName) {
        //when & then
        assertThrows(IllegalArgumentException.class, () -> new Customer(firstName, lastName));
    }

    @DisplayName("customer 생성 성공 테스트 - 성과 이름이 존재하면 생성에 성공한다")
    @Test
    void createSuccess() {
        //given
        String firstName = "ys";
        String lastName = "kim";
        //when
        Customer customer = assertDoesNotThrow(() -> new Customer(firstName, lastName));

        // then
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
    }

}