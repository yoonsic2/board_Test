package com._401unauthorized.board.controller;

import com._401unauthorized.board.dto.BoardDto;
import com._401unauthorized.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")

public class BoardController {
    public final BoardService bSer;

    //localhost/board/ or localhost/board
    @GetMapping({"/", ""})
    public String list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, Model model) {
        //서비스 -> DB -> 게시글들
        List<BoardDto> bList = bSer.getBoardList(pageNum);
        if(bList!=null){
            //페이지 정보
            String pageHtml = bSer.getPaging(pageNum);
            model.addAttribute("paging", pageHtml);
            model.addAttribute("bList", bList);  //js(json), each 문
            return "board/list";
        }
        return "redirect:/";
    }

    @GetMapping("/write")
    public String write() {
        return "board/write";
    }

    @PostMapping("/write")
    public String write(BoardDto board) {
        //DB에 글을 저장
        return "redirect:/board/list";
    }
}
