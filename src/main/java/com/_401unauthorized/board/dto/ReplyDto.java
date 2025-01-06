package com._401unauthorized.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class ReplyDto {
    //스프링 Validation
    private int r_num; //PK
    private int r_bnum; //FK(원글 번호)
    private String r_contents; //댓글 내용
    private String r_writer; //FK(작성자 id)
    private LocalDateTime r_date;
}
