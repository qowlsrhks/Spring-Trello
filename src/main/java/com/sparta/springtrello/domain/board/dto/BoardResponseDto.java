package com.sparta.springtrello.domain.board.dto;

import com.sparta.springtrello.domain.board.entity.Board;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardResponseDto {

    private Long id;
    private String boardTitle;
    private String boardDescription;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public BoardResponseDto(Board board) {
        this.id = board.getBoardId();
        this.boardTitle = board.getBoardTitle();
        this.boardDescription = board.getBoardDescription();
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

}
