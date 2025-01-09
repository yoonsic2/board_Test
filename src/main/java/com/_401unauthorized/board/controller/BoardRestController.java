package com._401unauthorized.board.controller;

import com._401unauthorized.board.common.FileManager;
import com._401unauthorized.board.dto.BoardFile;
import com._401unauthorized.board.dto.ReplyDto;
import com._401unauthorized.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController //@ResponseBody 기본값
@Slf4j

@RequestMapping("/board")
public class BoardRestController {
    @Autowired
    private BoardService bSer;
    @Autowired
    private FileManager fm;

    @PostMapping("/reply")
    //jackson(메세지컨버터) : json --> java 객체변환 @RequestBody
    //jackson(메세지컨버터) : json <-- java 객체변환 @ResponseBody
    public List<ReplyDto> insertReply(ReplyDto replyDto){ //, HttpSession session) {
        log.info("=====insert r_bnum:{}", replyDto.getR_bnum());
        log.info("=====insert r_contents:{}", replyDto.getR_contents());
        //String id = ((MemberDto)session.getAttribute("member")).getM_id();

        List<ReplyDto> rList = bSer.insertReply(replyDto);

        log.info("=====rList: {}", rList);
        return rList;
    }

    @GetMapping("/reply")
    public List<ReplyDto> getReplyList(@RequestParam("r_bnum") Integer bnum){
        List<ReplyDto> rList = bSer.getReplyList(bnum);
        log.info("=====rList:{}", rList);
        return rList; //ajax done() 리턴
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(BoardFile boardFile, HttpSession session){
        log.info("=====orifilename:{}", boardFile.getBf_orifilename());
        log.info("=====sysfilename:{}", boardFile.getBf_sysfilename());
        // 다운로드 메소드 호출 --> 브라우저 다운로드 됨.
        try {
            ResponseEntity<Resource> resp = fm.fileDownload(boardFile, session);
            return resp;
        } catch (IOException e) {
            log.info("*********파일 다운로드 예외");
            throw new RuntimeException(e);
        }
    }
}
