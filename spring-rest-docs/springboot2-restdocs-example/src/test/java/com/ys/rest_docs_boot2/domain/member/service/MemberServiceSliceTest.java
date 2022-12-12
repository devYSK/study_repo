package com.ys.rest_docs_boot2.domain.member.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ys.rest_docs_boot2.common.exception.DuplicateException;
import com.ys.rest_docs_boot2.domain.member.Member;
import com.ys.rest_docs_boot2.domain.member.MemberRepository;
import com.ys.rest_docs_boot2.domain.member.api.request.MemberCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceSliceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
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

        Member member = Member.builder()
            .name(request.getName())
            .nickName(request.getNickName())
            .age(request.getAge())
            .address(request.getAddress())
            .build();

        given(memberRepository.existsByNickName(nickName))
            .willReturn(false);

        given(memberRepository.save(member))
            .willReturn(member);

        //when
        memberService.createMember(request);

        //then
        verify(memberRepository).existsByNickName(nickName);
        verify(memberRepository).save(member);
    }

    @DisplayName("createMember - 닉네임이 중복되면 멤버 생성에 실패한다.")
    @Test
    void createMemberFailExistsNickname() {
        //given
        String name = "김영수";
        String address = "서울시 노원구";
        int age = 28;
        String nickName = "김별";

        MemberCreateRequest request
            = new MemberCreateRequest(name,
            nickName, age, address);

        given(memberRepository.existsByNickName(nickName))
            .willReturn(true);

        //when

        assertThrows(DuplicateException.class,
            () -> memberService.createMember(request));
        //then
        verify(memberRepository).existsByNickName(nickName);

    }

}