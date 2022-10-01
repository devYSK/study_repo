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


    private final LockerRepository lockerRepository;

    @Transactional
    void saveTest() {
        Member member = new Member();
        member.setId("1234");
        memberRepository.save(member);

        Locker locker = new Locker();
        locker.setId(1L);
        Locker save = lockerRepository.save(locker);
        member.setLocker(save);
        save.setMember(member);

//        MemberRepository
    }
}
