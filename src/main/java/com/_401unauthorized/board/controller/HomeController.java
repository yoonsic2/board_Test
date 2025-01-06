package com._401unauthorized.board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @GetMapping
    public String home(HttpSession session) {
        // 검증할 코드
        // 세션의 불필요한 속성 객체는 즉시 삭제하거나 루트에서 삭제할것.
        if (session.getAttribute("urlPrior_login") == null) {
            session.removeAttribute("urlPrior_login");
        }
        session.removeAttribute("sDto");
        session.removeAttribute("pageNum");
        return "index"; //index.html
    }


}
