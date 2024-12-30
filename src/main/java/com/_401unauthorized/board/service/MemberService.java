package com._401unauthorized.board.service;

import com._401unauthorized.board.dao.MemberDao;
import com._401unauthorized.board.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  //final 대한 생성자 주입
@Slf4j

public class MemberService {
    //@Autowired
    private final MemberDao mDao;
    public MemberDto login(MemberDto mDto) {
        String encoPw = mDao.getSecurityPw(mDto.getM_id());
        log.info ("encoPw:{}" + encoPw);
        if(encoPw!=null) {
            BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
            log.info("==== 아이디 존재함");
            if(pwEncoder.matches(mDto.getM_pw(), encoPw)) {
                log.info("==== 로그인 성공");
                return mDao.getMemberInfo(mDto.getM_id());
            }else{
                log.info("==== 비번 오류");
                return null;
            }
        }else{
            log.info("==== 아이디 오류");
            return null;
        }
    }

    public boolean join(MemberDto mDto) {
        //Encoder(암호화) <--> Decoder(복호화)

        //이미 사용중인 ID: true
        if(mDao.isUsedId(mDto.getM_id())){
            return false; // 회원가입 실패
        }
        BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        mDto.setM_pw(pwEncoder.encode(mDto.getM_pw()));

        return mDao.join(mDto);  // 회원가입 성공시: true, 실패하면: false
    }
}
