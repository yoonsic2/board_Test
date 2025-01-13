package com._401unauthorized.board.dao;

import com._401unauthorized.board.dto.MemberDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberDaoTest {
    @Autowired
    private MemberDao memberDao;

    @DisplayName("회원가입 테스트")
    @Test
    @Transactional //test가 끝나면 rollback 처리
    void joinTest(){
        MemberDto mb = new MemberDto();
        mb.setM_id("xxx").setM_pw("1111").setM_name("엑스엑스").setM_birth("20001010").setM_address("인천");
        assertTrue(memberDao.join(mb));
        //assertEquals(true, memberDao.join(mb));
        //Assertions.assertEquals(true, memberDao.join(mb));
    }

    // jUnit: 단위(클래스) 테스트 라이브러리
    @Test
    @DisplayName("회원리스트 반환 테스트")
    public void getAllMemberTest(){
        List<MemberDto> mList = memberDao.getAllMember();
        assertThat(mList.size()).isEqualTo(10);
        assertThat(mList.size()).isLessThan(200);
    }
}
