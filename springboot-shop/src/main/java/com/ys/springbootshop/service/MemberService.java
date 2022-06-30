package com.ys.springbootshop.service;

import com.ys.springbootshop.entity.Member;
import com.ys.springbootshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ysk
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail())
                .isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
