package com.ys.servlet.web.frontcontroller.v4;

import java.util.Map;

/**
 * @author : ysk
 */
public class MemberFormControllerV4 implements ControllerV4{

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        return "new-form";
    }

}
