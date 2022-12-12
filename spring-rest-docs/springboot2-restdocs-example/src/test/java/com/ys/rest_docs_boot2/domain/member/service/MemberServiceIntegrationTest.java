package com.ys.rest_docs_boot2.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import com.ys.rest_docs_boot2.common.exception.DuplicateException;
import com.ys.rest_docs_boot2.domain.member.Member;
import com.ys.rest_docs_boot2.domain.member.MemberRepository;
import com.ys.rest_docs_boot2.domain.member.api.request.MemberCreateRequest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("createMember - 멤버 생성에 성공한다.")
    @Test
    void createMemberSuccess() {
        //given
        String name = "김영수";
        String address = "서울시 노원구";
        int age = 28;
        String nickName = "김별";

        MemberCreateRequest request
            = new MemberCreateRequest(name,
            nickName, age, address);

        //when
        Long createMemberId = memberService.createMember(request);

        //then
        assertNotNull(createMemberId);
        Optional<Member> memberOptional = memberRepository.findById(createMemberId);
        assertTrue(memberOptional.isPresent());
        Member findMember = memberOptional.get();
        assertThat(findMember)
            .hasFieldOrPropertyWithValue("name", name)
            .hasFieldOrPropertyWithValue("address", address)
            .hasFieldOrPropertyWithValue("age", age)
            .hasFieldOrPropertyWithValue("nickName", nickName);
    }

    @DisplayName("createMember - 닉네임이 중복되면 멤버 생성에 실패한다.")
    @Test
    void createMemberFailExistsNickname() {
        //given
        String name = "김영수";
        String address = "서울시 노원구";
        int age = 28;
        String nickName = "김별";

        Member member = Member.builder()
            .name(name)
            .nickName(nickName)
            .age(age)
            .address(address)
            .build();

        memberRepository.save(member);

        MemberCreateRequest request
            = new MemberCreateRequest(name,
            nickName, age, address);

        //when & then
        assertThrows(DuplicateException.class,
            () -> memberService.createMember(request));

    }

}