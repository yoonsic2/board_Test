package com._401unauthorized.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class ReplyDto {
    private int r_bnum;
    private String r_contents;
    private String r_writer;
}
