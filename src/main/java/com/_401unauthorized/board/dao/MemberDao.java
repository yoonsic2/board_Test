package com._401unauthorized.board.dao;

//DB FW: ibatis => mybatis
import com._401unauthorized.board.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper

public interface MemberDao {

    // 짧은놈은 = @Select("SELECT....")
    @Select("SELECT COUNT(*) FROM MEMBER WHERE M_ID=#{m_id} AND M_PW=#{m_pw}")
    boolean login(MemberDto mDto);

    // 길다? xml
    boolean join(MemberDto mDto);

    //@Select("SELECT COUNT(*) FROM MEMBER WHERE M_ID=#{id}")
    boolean isUsedId(String id);

    MemberDto getMemberInfo(String mId);

    String getSecurityPw(String mId);

    void updateMemberPoint(MemberDto memberDto);

    //jUnit 테스트용
    @Select("SELECT * FROM MEMBER")
    List<MemberDto> getAllMember();
}
