package com.ys.jpabookstudy.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ysk
 */
@Service
@RequiredArgsConstructor
public class TestService {

    private final MemberRepository memberRepository;

}
