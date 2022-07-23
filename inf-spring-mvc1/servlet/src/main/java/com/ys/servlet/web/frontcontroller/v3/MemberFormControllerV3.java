package com.ys.servlet.web.frontcontroller.v3;

import com.ys.servlet.web.frontcontroller.ModelView;

import java.util.Map;

/**
 * @author : ysk
 */
public class MemberFormControllerV3 implements ControllerV3 {

    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }

}
