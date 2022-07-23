package com.ys.servlet.web.frontcontroller.v4;

import com.ys.servlet.domain.Member;
import com.ys.servlet.domain.MemberRepository;

import java.util.List;
import java.util.Map;

/**
 * @author : ysk
 */
public class MemberListControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object>
            model) {
        List<Member> members = memberRepository.findAll();
        model.put("members", members);
        return "members";
    }

}