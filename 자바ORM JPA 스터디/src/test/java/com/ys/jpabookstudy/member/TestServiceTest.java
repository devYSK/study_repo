package com.ys.jpabookstudy.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

/**
 * @author : ysk
 */
@SpringBootTest
class TestServiceTest {

    @Autowired
    TestService testService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void find() {
        Optional<Member> byId = memberRepository.findById("1234");

        Member member = byId.get();
        System.out.println();
    }
    @Test
    void test() {
        testService.saveTest();
    }
}