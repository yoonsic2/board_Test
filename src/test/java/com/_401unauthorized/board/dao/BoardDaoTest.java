package com._401unauthorized.board.dao;

import com._401unauthorized.board.dto.BoardDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

public class BoardDaoTest {
    @Autowired                  //  junit 테스트시 필드주입만 가능.
    private BoardDao boardDao;

    @Test
    public void initTest(){
        assertNotNull(boardDao);
    }

    @Test
    void insertDummyDataTest(){
        BoardDto bDto = new BoardDto();
        for(int i=0; i<35; i++){
            bDto.setB_title("제목"+i).setB_contents("무궁화 꽃이 피었습니다.")
                    .setB_writer("dbstlr2");
            boardDao.insertDummyData(bDto);
        }
    }

    @Test
    public void findBoardListTest(){
        assertEquals(35, boardDao.getBoardListAll().size());
        boardDao.getBoardListAll().stream().forEach(bDto -> System.out.println(bDto));
    }
}
