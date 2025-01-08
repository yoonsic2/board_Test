package com._401unauthorized.board.controller;

import com._401unauthorized.board.dto.BoardDto;
import com._401unauthorized.board.dto.SearchDto;
import com._401unauthorized.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public String list(SearchDto sDto, Model model, HttpSession session) {
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

            //상세보기에서 게시글 목록으로 돌아가기 위해서
            if(sDto.getColname()!=null) {
                session.setAttribute("sDto", sDto);
                log.info("=====검색중이였다면 sDto 세션에 저장");
            }else {
                session.removeAttribute("sDto");
                //페이지 번호만 저장
                session.setAttribute("pageNum", sDto.getPageNum());
            }
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

    @GetMapping("/write")
    public String write() {
        return "board/write";
    }

    @PostMapping("/write")
    //public String write(BoardDto board, @RequestPart List<MultipartFile> attachments) {
    public String write(BoardDto board) {
        log.info("왔냐?????????????????????????????????????????????????");
//        log.info("=====write board:{}", board);  //b_writer, b_title, b_contents
//        log.info("=====write attachments:{}", attachments.size());  //첨부파일 명
//        for (MultipartFile file : attachments) {
//            log.info("=====file:{}", file.getOriginalFilename());
//        }
//        return "redirect:/board/list";
        log.info("=====write board:{}", board);
        log.info("=====write board.attachments:{}", board.getAttachments().size());
        for(MultipartFile file : board.getAttachments()) {
            log.info("=====file:{}", file.getOriginalFilename());
            log.info("=====file.getSize():{}", file.getSize());
        }
        return "redirect:/board";
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
        BoardDto board = bSer.getBoardDetail(b_num); //원글 1개
        log.info("board:{}", board); // 제목, 글쓴이, 내용, 날짜. 조회수
        if (board == null) {
            return "redirect:/board";
        } else {
            //List<ReplyDto> rList = bSer.getReplyList(b_num); // 댓글 n개
            model.addAttribute("board", board);
            //model.addAttribute("rList", rList);
            return "board/detail";
        }
    }
}