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

import java.lang.reflect.Member;

@Controller
@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    //    @Autowired
    private final MemberService mSer;
    @GetMapping("/login")
    public String login(){
        return "member/login";
    }
    @PostMapping("/login")
    public String login(MemberDto memberDto, HttpSession session,RedirectAttributes rttr){
        log.info("id:{}, pwd:{}",memberDto.getM_id(),memberDto.getM_pw());
        //DB에서 select
        //MemberDto memberDto = new MemberDto();
        //MemberDto.setM_id(m_id).setM_pwd(m_pwd);
//        MemberDto memberDto = MemberDto.builder().m_id(m_id).m_pwd(m_pwd).build();
        MemberDto member =mSer.login(memberDto);
        log.info("=======member:{}",member);
        if(member!=null){
//            session.setAttribute("id",memberDto.getM_id());
            session.setAttribute("member",member);
            Object url = session.getAttribute("urlPrior_login");
            if(url!=null){
                session.removeAttribute("urlPrior_login");
                return "redirect:"+url.toString();
            }
//            rttr.addAttribute("msg","로그인 실패");//request 객체에 저장
            //session영역저장-->request영역 저장--> 1번 사용 후 자동 삭제

            return "redirect:/board";
        }
        rttr.addFlashAttribute("msg","로그인 실패");
        return "index";
    }
    @GetMapping("/join")
    public String join(HttpSession  session){
        //인가(권한)여부를 너무 많이 확인해야함 -->인터셉터 or 시큐리티
        //로그인 상태라면 index.html
        if(session.getAttribute("member")!=null){
            return "redirect:/"; //index.html
        }
        return "member/join";
    }
    @PostMapping("/join")
    public String join(MemberDto memberDto, RedirectAttributes rttr){
        log.info("========memberDto:{}",memberDto);
        boolean result=mSer.join(memberDto);
        if(result){
//            model.addAttribute("msg","가입성공");
            rttr.addFlashAttribute("msg","가입성공");
            return "redirect:/";
        }else{
            rttr.addFlashAttribute("msg","가입실패");
            return "redirect:/member/join";
        }

    }
    @PostMapping("/logout")
    public String logout(HttpSession httpSession, RedirectAttributes rttr){
        httpSession.invalidate();
        rttr.addFlashAttribute("msg","로그아웃 성공");
        return "redirect:/";
    }

}