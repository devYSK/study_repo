package com.group.libraryapp.calculator

import org.junit.jupiter.api.*

class JunitTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            println("모든 테스트 시작 전")
        } @JvmStatic
        @AfterAll
        fun afterAll() {
            println("모든 테스트 실행 후")
        }
    }

    
    @BeforeEach
    fun beforeEach() {
        println("각 테스트 시작 전")
    }

    @AfterEach
    fun afterEach() {
        println("각 테스트 실행 후")
    }

    @Test
    fun test1() {
        println("테스트 1")
    }

    @Test
    fun test2() {
        println("테스트 2")
    }
}