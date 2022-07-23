package com.ys.servlet.web.frontcontroller.v3;

import com.ys.servlet.web.frontcontroller.ModelView;

import java.util.Map;

/**
 * @author : ysk
 */
public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);

}
