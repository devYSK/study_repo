package com.ys.servlet.web.frontcontroller.v3;

import com.ys.servlet.domain.Member;
import com.ys.servlet.domain.MemberRepository;
import com.ys.servlet.web.frontcontroller.ModelView;

import java.util.List;
import java.util.Map;

/**
 * @author : ysk
 */
public class MemberListControllerV3 implements ControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        List<Member> members = memberRepository.findAll();
        ModelView mv = new ModelView("members");
        mv.getModel().put("members", members);
        return mv;
    }
}