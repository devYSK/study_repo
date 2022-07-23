package com.ys.servlet.web.frontcontroller.v4;

import java.util.Map;

/**
 * @author : ysk
 */
public interface ControllerV4 {

    String process(Map<String, String> paramMap, Map<String, Object> model);

}
