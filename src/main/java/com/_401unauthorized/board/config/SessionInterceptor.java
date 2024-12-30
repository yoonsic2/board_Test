package com._401unauthorized.board.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class SessionInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("id") == null) {
            log.info("====인터셉트! 로그인 안 함!");
            response.sendRedirect("/member/login");
            return false; // 컨트롤러 진입 금지
        }
        return true;
        // return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
    }
}

    // 나중에 써볼것
    //@Override
