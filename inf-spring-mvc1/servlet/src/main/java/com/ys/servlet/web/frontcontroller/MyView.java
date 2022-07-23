package com.ys.servlet.web.frontcontroller;

import lombok.AllArgsConstructor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author : ysk
 */
@AllArgsConstructor
public class MyView {

    private String viewPath;

    public void render(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);

    }


    public void render(Map<String, Object> model, HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        modelToRequestAttribute(model, request);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);

        requestDispatcher.forward(request, response);

    }

    private void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {
        model.forEach(request::setAttribute);
    }
}
