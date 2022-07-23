package com.ys.servlet.web.frontcontroller.v5;

import com.ys.servlet.web.frontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : ysk
 */
public interface MyHandlerAdapter {

    boolean supports(Object handler); // 어댑터가 해당 컨트롤러를 처리할 수 있는지 판단하는 메서드다.

    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws ServletException, IOException;

}
