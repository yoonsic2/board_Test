package com._401unauthorized.board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping
    public String home(HttpSession session) {
        if (session.getAttribute("msg") == null) {
            session.removeAttribute("msg");
        }
        return "index"; //index.html
    }


}
