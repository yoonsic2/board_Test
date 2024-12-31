package com._401unauthorized.board.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class SessionInterceptor implements AsyncHandlerInterceptor {
    // 요청1: /board/list --> 인터셉트 --> 요청2: 로그인 성공 --> /board/list
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("====preHandle call uri={}", request.getRequestURI());
        log.info("====preHandle call queryString={}", request.getQueryString());

        HttpSession session = request.getSession();
        if (session.getAttribute("member") == null) {
            log.info("====인터셉트! 로그인 안 함!");
            session.setAttribute("urlPrior_login", request.getRequestURI()+'?'+request.getQueryString());
            response.sendRedirect("/member/login");
            return false; // 컨트롤러 진입 금지
        }
        return true;
        // return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
    }
}

    // 나중에 써볼것
    //@Override
