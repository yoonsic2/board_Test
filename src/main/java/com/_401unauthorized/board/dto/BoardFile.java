package com._401unauthorized.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//MyBatis의 resultType, @ModelAttribute 에서는 반드시 디폴트 생성자 필요함

// 테이블과 일치하는 클래스 Entity
public class BoardFile {
    private long bf_num;
    private long bf_bnum;
    private String bf_orifilename;
    private String bf_sysfilename;

}
