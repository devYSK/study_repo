package com.ys.servlet.web.frontcontroller.v3;

import com.ys.servlet.domain.Member;
import com.ys.servlet.domain.MemberRepository;
import com.ys.servlet.web.frontcontroller.ModelView;

import java.util.Map;

/**
 * @author : ysk
 */
public class MemberSaveControllerV3 implements ControllerV3{


    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {

        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);

        memberRepository.save(member);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        return mv;
    }

}
