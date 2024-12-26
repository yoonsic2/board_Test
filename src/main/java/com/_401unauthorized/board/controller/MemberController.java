package com._401unauthorized.board.controller;

import com._401unauthorized.board.dto.MemberDto;
import com._401unauthorized.board.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class MemberController {

    @Autowired
    private MemberService mSer;

    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @PostMapping("/member/login")
    public String login(@RequestParam String m_id, @RequestParam String m_pw) {
        log.info("id : {}, pw : {}", m_id, m_pw);
        //DB에서 select
        MemberDto mDto = MemberDto.builder().m_id(m_id).m_pw(m_pw).build();
        boolean result = mSer.login(mDto);
        if (result) {
            return "board/list";
        }
        return "index";
    }

}
