package com._401unauthorized.board.controller;

import com._401unauthorized.board.dto.ReplyDto;
import com._401unauthorized.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //@ResponseBody 기본값
@Slf4j

@RequestMapping("/board")
public class BoardRestController {
    @Autowired
    private BoardService bSer;
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

}
