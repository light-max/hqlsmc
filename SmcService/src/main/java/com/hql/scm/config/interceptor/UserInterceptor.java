package com.hql.scm.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object admin = request.getSession().getAttribute("user");
        if (admin == null) {
            response.sendRedirect("/user/notloggedin");
            return false;
        }
        return true;
    }
}
