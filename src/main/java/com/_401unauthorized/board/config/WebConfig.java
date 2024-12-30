package com._401unauthorized.board.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//레거시 프로젝트 환경설정 .xml
@Configuration // boot 환경설정 java config

public class WebConfig implements WebMvcConfigurer {
    @Autowired
    SessionInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                // 컨트롤러에서
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/member/login", "/member/logout")
                .excludePathPatterns("/member/join")
                .excludePathPatterns("/js/**", "/css/**", "/img/**");
    }
}