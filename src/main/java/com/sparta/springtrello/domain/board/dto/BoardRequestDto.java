package com.sparta.springtrello.domain.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardRequestDto {

    private String boardTitle;
    private String boardDescription;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
