package com._401unauthorized.board.dao;

import com._401unauthorized.board.dto.BoardDto;
import com._401unauthorized.board.dto.SearchDto;
import org.apache.ibatis.annotations.Delete;
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
    List<BoardDto> getBoardListAll(Map<String, Integer> pageMap);

    int getBoardCount(SearchDto sDto);

    List<BoardDto> getBoardListSearch(SearchDto sDto);

    BoardDto getBoardDetail(Integer bNum);

    @Delete("DELETE FROM BOARD WHERE B_NUM=#{bNum}")
    boolean boardDelete(Integer bNum);
}
