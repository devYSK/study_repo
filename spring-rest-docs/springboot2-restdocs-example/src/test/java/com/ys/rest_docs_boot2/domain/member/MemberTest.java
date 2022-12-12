package com.ys.rest_docs_boot2.domain.member;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {


    @DisplayName("멤버 생성 실패 테스트 - 이름이 빈 값이거나 길이가 2보다 작으면 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "1", "a", "즐", "일", "킹", "더"})
    void createFailNameIsNullOrSize(String name) {
        //given
        String nickName = "날강두가 한반두";
        int age = 28;
        String address = "서울시 노원구";

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> new Member(name, nickName, age, address));

    }

    @DisplayName("멤버 생성 실패 테스트 - 닉네임이 빈 값이거나 길이가 2보다 작으면 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "1", "a", "즐", "일", "킹", "더"})
    void createFailNickNameIsNullOrSize(String nickName) {
        //given
        String name = "김호돈";
        int age = 28;
        String address = "서울시 노원구";

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> new Member(name, nickName, age, address));

    }

    @DisplayName("멤버 생성 실패 테스트 - age가 1보다 작으면 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4, -5, -6})
    void createFailAgeMin(int age) {
        //given
        String name = "김호돈";
        String nickName = "호돈신과 한반두";
        String address = "서울시 노원구";

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> new Member(name, nickName, age, address));
    }

    @DisplayName("멤버 생성 실패 테스트 - 주소가 빈 값이면 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createFailAddressIsEmpty(String address) {
        //given
        String name = "김호돈";
        String nickName = "호돈신과 한반두";
        int age = 30;

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> new Member(name, nickName, age, address));
    }

    @DisplayName("멤버 생성 성공 테스트")
    @Test
    void createSuccess() {
        //given
        String name = "김호돈";
        String nickName = "호돈신과 한반두";
        String address = "서울시 노원구";
        int age = 28;

        //when & then
        Member member = assertDoesNotThrow(() -> new Member(name, nickName, age, address));

        assertEquals(name, member.getName());
        assertEquals(nickName, member.getNickName());
        assertEquals(address, member.getAddress());
        assertEquals(age, member.getAge());
    }

}