package com._401unauthorized.board.service;

import com._401unauthorized.board.common.Paging;
import com._401unauthorized.board.dao.BoardDao;
import com._401unauthorized.board.dto.BoardDto;
import com._401unauthorized.board.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j

public class BoardService {
    public static final Integer LISTCNT = 10 ;
    private static final int PAGECOUNT = 2;
    private final BoardDao boardDao;
    public List<BoardDto> getBoardList(Integer pageNum) {

        //select * from board order b_date desc limit 0,10
        //select * from board order b_date desc limit 10,20
        //select * from board order b_date desc limit 20,10
        Map<String,Integer> pageMap = new HashMap<>();
        pageMap.put("startIndex",(pageNum-1)*10);
        pageMap.put("pageSize",10);
        return boardDao.getBoardList(pageMap);

    }

    public String getPaging(SearchDto sDto) {
        int totalNum =boardDao.getBoardCount(sDto);
        log.info("===========totalNum :" + totalNum);
        String listUrl = null;
        if(sDto.getColname()!=null){
            listUrl="/board?colname="+sDto.getColname()+"&keyword="+sDto.getKeyword(); //&pageNum=2
        }else{
            listUrl = "/board?"; //pageNum=2
        }
        Paging paging = new Paging(totalNum,sDto.getPageNum(),sDto.getListcnt(),PAGECOUNT,listUrl);
        return paging.makeHtmlPaging();
    }

    public List<BoardDto> getBoardList(SearchDto sDto) {
        Integer pageNum = sDto.getPageNum();
        //페이지 번호를 limit 시작 번호로 변경 1p:idx(0)~, 2p:idx(10)~
        sDto.setStartIdx((pageNum-1)* BoardService.LISTCNT);
        return boardDao.getBoardListSearch(sDto);
    }

    public List<BoardDto> getBoardListSearch(SearchDto sDto) {
        Integer pageNum =sDto.getPageNum();
//        sDto.setStartIdx((pageNum-1)*LISTCNT);
        sDto.setStartIdx((pageNum-1)* sDto.getListcnt());
        return boardDao.getBoardListSearch(sDto);
    }

    public List<BoardDto> getBoardListSearchNew(SearchDto sDto) {
        Integer pageNum =sDto.getPageNum();
//        sDto.setStartIdx((pageNum-1)*LISTCNT);
        sDto.setStartIdx((pageNum-1)* sDto.getListcnt());
        return boardDao.getBoardListSearch(sDto);
    }

    public BoardDto getBoardDetail(Integer bNum) {
        return boardDao.getBoardDetail(bNum);
    }

    public boolean boardDelete(Integer bNum) {
        return boardDao.boardDelete(bNum);
    }
}