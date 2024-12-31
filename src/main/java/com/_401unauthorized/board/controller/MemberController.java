package com._401unauthorized.board.controller;

import com._401unauthorized.board.dto.MemberDto;
import com._401unauthorized.board.service.MemberService;
import jakarta.servlet.http.HttpSession;
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
    public String login(HttpSession session) {
        if(session.getAttribute("member") != null) {
            return "redirect:/";  // 로그인 상태라면 index.html 로 리다이렉트
        }
        // 인가(권한) 여부를 너무 많이 확인해야 됨. --> 인터셉터 활용 or 시큐리티
        return "member/login";
    }

    @PostMapping("/login")
    public String login(MemberDto mDto, HttpSession session, RedirectAttributes rttr) {
        log.info("id : {}, pw : {}", mDto.getM_id(), mDto.getM_pw());

        //DB에서 select
        //MemberDto mDto = MemberDto.builder().m_id(m_id).m_pw(m_pw).build();

        //boolean result = mSer.login(mDto);
        MemberDto member = mSer.login(mDto);
        log.info("====member: {}", member);
        if (member != null) {
            //session.setAttribute("id", mDto.getM_id());
            session.setAttribute("member", member);
            Object url = session.getAttribute("urlPrior_login");
            if(url!=null) {
                session.removeAttribute("urlPrior_login");
                return "redirect:"+url.toString();
            }
            return "redirect:/board";
        } // end login 성공
        //rttr.addAttribute("msg", "로그인실패"); //request 객체에 저장
        rttr.addFlashAttribute("msg", "로그인 실패");
        //session 영역 저장 --> request 영역 저장 --> 1번 사용후 자동삭제
        return "redirect:/";
    }

    @GetMapping("/join")
    public String join(HttpSession session) {
        if(session.getAttribute("member") != null) {
            return "redirect:/";  // index.html 포워딩
        }
        return "member/join";  //Get이야?  포워딩 이거나 DB에 가서 select한다
    }

    @PostMapping("/join")
    public String join(MemberDto mDto, Model model, RedirectAttributes rttr, HttpSession session) {  // Post야?  insert update delete
        log.info("==========memberDto:{}", mDto);
        boolean result = mSer.join(mDto);
        if (result) {
            //session.setAttribute("msg", "가입성공");
            //model.addAttribute("msg", "가입성공");
            rttr.addFlashAttribute("msg", "가입성공");
            //session영역에 저장후 1번 사용후 session영역에서 삭제함.
            return "redirect:/";
            //return "redirect:/"; //localhost/ --> index.html
        }
        //model.addAttribute("msg", "가입실패");
        rttr.addFlashAttribute("msg", "가입실패");
        return "redirect:/member/join";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes rttr) {
        session.invalidate();  //세션 무효화
        rttr.addFlashAttribute("msg", "로그아웃 성공");
        return "redirect:/";
    }

}//con end
