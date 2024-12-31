package com._401unauthorized.board.service;

import com._401unauthorized.board.common.Paging;
import com._401unauthorized.board.dao.BoardDao;
import com._401unauthorized.board.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class BoardService {
    private final BoardDao boardDao;

    public List<BoardDto> getBoardList(Integer pageNum) {
        //SELECT * FROM BOARD ORDER B_DATE DESC LIMIT 0, 10 , 1page
        //SELECT * FROM BOARD ORDER B_DATE DESC LIMIT 10, 10 , 1page
        //SELECT * FROM BOARD ORDER B_DATE DESC LIMIT 20, 10 , 1page
        Map<String, Integer> pageMap = new HashMap<>();
        pageMap.put("startIndex", (pageNum -1) * 10);
        pageMap.put("pageSize", 10);

        return boardDao.getBoardList(pageMap);
    }

    public String getPaging(Integer pageNum) {
        int totalNum = boardDao.getBoardCount();
        Paging paging = new Paging(totalNum, pageNum, 10, 2, "/board?");

        return paging.makeHtmlPaging(); //return "<a href=''>1</a> .....
    }
}
