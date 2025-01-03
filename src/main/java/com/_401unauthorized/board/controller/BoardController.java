package com._401unauthorized.board.controller;

import com._401unauthorized.board.dto.BoardDto;
import com._401unauthorized.board.dto.MemberDto;
import com._401unauthorized.board.dto.ReplyDto;
import com._401unauthorized.board.dto.SearchDto;
import com._401unauthorized.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j

public class BoardController {
    public final BoardService bSer;

    //localhost/board/ or localhost/board
    @GetMapping({"/", ""})
    public String list(SearchDto sDto, Model model) {
        log.info("=====before sDto:{}", sDto);
        //서비스-->DB -->게시글들
        if (sDto.getPageNum() == null) {
            sDto.setPageNum(1);
        }
        if (sDto.getListcnt() == null) {
            sDto.setListcnt(BoardService.LISTCNT);
        }
        if (sDto.getStartIdx() == null) {
            sDto.setStartIdx(0);
        }
        List<BoardDto> bList = null;
//        if(sDto.getColname()==null || sDto.getKeyword()==null){
//           bList=bSer.getBoardList(sDto.getPageNum()); //페이징 클릭
//        }else{
//           bList=bSer.getBoardList(sDto);
//        }

        //동적 쿼리 작성시
        //검색 또는 페이지 번호 버튼 클릭 시
        bList = bSer.getBoardListSearchNew(sDto);


        if (bList != null) {
            //페이지 정보

            String pageHtml = bSer.getPaging(sDto);
            model.addAttribute("paging", pageHtml);
            model.addAttribute("bList", bList); //js(json), each문

            return "board/list";
        }
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String boardDelete(@RequestParam("b_num") Integer b_num, RedirectAttributes rttr) {
        log.info("=====delete b_num:{}", b_num);
        if (b_num == null || b_num < 1) {
            return "redirect:/board";
        }
        if(bSer.boardDelete(b_num)){
            rttr.addFlashAttribute("msg", b_num+"번 삭제 성공"); //1번만 쓰고 삭제돼
            //rttr.addAttribute("msg", b_num+"번 삭제 성공"); //여러번출력
//            return "redirect:/board?pageNum=1";
            return "redirect:/board"; //기본값 1
        }else{
            rttr.addAttribute("msg", b_num+"번 삭제 실패");
            return "redirect:/board/detail?b_num="+b_num;
        }
    }

    @PostMapping("/reply")
    @ResponseBody
    public String insertReply(@RequestBody ReplyDto replyDto, HttpSession session) {
        log.info("=====insert r_bnum:{}", replyDto.getR_bnum());
        log.info("=====insert r_contents:{}", replyDto.getR_contents());
        String id = ((MemberDto)session.getAttribute("member")).getM_id();
        log.info("=====insert r_writer:{}", id);
        return "성공";
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

    @GetMapping("/detail")
    public String detailParam(@RequestParam("b_num") Integer b_num, Model model) {
        log.info("===con bnum:{}", b_num);
        return null;
    }

    @GetMapping("/detail/{b_num}") //.../board/detail/80/aaa
    public String detail(@PathVariable("b_num") Integer b_num, Model model) {
        log.info("========detail b_num={}", b_num);
        //페이지 넘버가 없거나 음수일때
        if (b_num == null || b_num < 1) {
            return "redirect:/board";
        }
        BoardDto board = bSer.getBoardDetail(b_num);
        log.info("board:{}", board); // 제목, 글쓴이, 내용, 날짜. 조회수
        if (board == null) {
            return "redirect:/board";
        } else {
            model.addAttribute("board", board);
            return "board/detail";
        }
    }
}