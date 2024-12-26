package com._401unauthorized.board.service;

import com._401unauthorized.board.dao.MemberDao;
import com._401unauthorized.board.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class MemberService {
    @Autowired
    private MemberDao mDao;
    public boolean login(MemberDto mDto) {
        return mDao.login(mDto);
    }
}
