package com._401unauthorized.board.dao;

import com._401unauthorized.board.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper

public interface BoardDao {
    void insertDummyData(BoardDto bDto);

    ArrayList<BoardDto> getBoardList(Map<String, Integer> pageMap);

    @Select("SELECT * FROM BOARD")
    List<BoardDto> getBoardListAll();

    @Select("SELECT COUNT(*) FROM BOARD")
    int getBoardCount();
}
