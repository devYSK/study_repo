package com.ys.servlet.web.frontcontroller.v1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : ysk
 */
public interface ControllerV1 {

    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException;
}
