package com._401unauthorized.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Accessors(chain = true)

public class MemberDto {
    private String m_id;  //필드명 == 파라미터명 == (테이블)컬럼명
    private String m_pw;
    private String m_name;
    private String m_birth;
    private String m_addr;

    private String m_point;
}
