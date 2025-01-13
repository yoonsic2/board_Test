package com._401unauthorized.board.service;

import com._401unauthorized.board.dto.BoardDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BoardServiceTest {
    private static final Logger log = LoggerFactory.getLogger(BoardServiceTest.class);
    @Autowired
    private BoardService boardService;

    @DisplayName("종속 주입(DI) 테스트")
    @Test
    public void testDI(){
        log.info("***bSer*******:{}", boardService);
        Assertions.assertNotEquals(null, boardService);
        //Assertions.assertEquals(null, boardService);
    }

    @Test
    public void getBoardList(){
        //페이지 번호 설정 호출 후 글 리스트를 콘솔에 출력, 개수 출력
        List<BoardDto> bDto = boardService.getBoardList(1);
        //람다식
        bDto.forEach(b->log.info("b:{}",b));
        //for (BoardDto boardDto : bDto) {
        //log.info("**********boardDto: {}", boardDto);
        //}
        //개수 비교
        Assertions.assertEquals(10, bDto.size());
    }
}
