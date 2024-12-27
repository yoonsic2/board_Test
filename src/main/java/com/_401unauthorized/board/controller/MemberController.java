package com._401unauthorized.board.controller;

import com._401unauthorized.board.dto.MemberDto;
import com._401unauthorized.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor  //final 필드에 대한 생성자

public class MemberController {
    //@Autowired
    private final MemberService mSer;

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @PostMapping("/login")
    public String login(MemberDto mDto) {
        log.info("id : {}, pw : {}", mDto.getM_id(), mDto.getM_pw());

        //DB에서 select
        //MemberDto mDto = MemberDto.builder().m_id(m_id).m_pw(m_pw).build();

        boolean result = mSer.login(mDto);
        if (result) {
            return "board/list";
        }
        return "index";
    }

    @GetMapping("/join")
    public String join() {
        return "member/join";  //Get이야?  포워딩 이거나 DB에 가서 select한다
    }

    @PostMapping("/join")
    public String join(MemberDto mDto, Model model, RedirectAttributes rttr) {  // Post야?  insert update delete
        log.info("==========memberDto:{}", mDto);
        boolean result = mSer.join(mDto);
        if (result) {
            //model.addAttribute("msg", "가입성공");
            rttr.addFlashAttribute("msg", "가입성공");
            //session영역에 저장후 1번 사용후 session영역에서 삭제함.
            return "redirect:/"; //localhost/ --> index.html
        }
        model.addAttribute("msg", "가입실패");
        return "member/join";
    }

}//con end
