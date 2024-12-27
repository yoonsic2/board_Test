package com._401unauthorized.board.service;

import com._401unauthorized.board.dao.MemberDao;
import com._401unauthorized.board.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  //final 대한 생성자 주입

public class MemberService {
    //@Autowired
    private final MemberDao mDao;
    public boolean login(MemberDto mDto) {
        return mDao.login(mDto);
    }

    public boolean join(MemberDto mDto) {
        return mDao.join(mDto);
    }
}
