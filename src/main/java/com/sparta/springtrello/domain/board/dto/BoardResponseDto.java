package com.sparta.springtrello.domain.board.dto;

import com.sparta.springtrello.domain.board.entity.Board;
import lombok.Data;


@Data
public class BoardResponseDto {

    private Long id;
    private String boardTitle;
    private String boardDescription;

    public BoardResponseDto(Board board) {
        this.id = board.getBoardId();
        this.boardTitle = board.getBoardTitle();
        this.boardDescription = board.getBoardDescription();
    }

}
