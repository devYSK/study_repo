package com.ys.rest_docs_boot2.domain.member.service;

import com.ys.rest_docs_boot2.common.exception.DuplicateException;
import com.ys.rest_docs_boot2.domain.member.Member;
import com.ys.rest_docs_boot2.domain.member.MemberRepository;
import com.ys.rest_docs_boot2.domain.member.api.request.MemberCreateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long createMember(MemberCreateRequest request) {

        validateExistsByNickName(request.getNickName());

        Member member = Member.builder()
            .name(request.getName())
            .nickName(request.getNickName())
            .age(request.getAge())
            .address(request.getAddress())
            .build();

        return memberRepository.save(member).getId();
    }

    private void validateExistsByNickName(String nickName) {
        Assert.hasText(nickName, "닉네임은 빈 값이여서는 안됩니다.");
        boolean exists = memberRepository.existsByNickName(nickName);
        if (exists) {
            throw new DuplicateException("이미 존재하는 닉네임입니다.", nickName);
        }
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
