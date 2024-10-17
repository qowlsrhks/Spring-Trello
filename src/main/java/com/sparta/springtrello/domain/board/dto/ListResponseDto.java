package com.sparta.springtrello.domain.board.dto;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.cardList.dto.CardListResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class ListResponseDto {

    private Long id;
    private String boardTitle;
    private String boardDescription;
    private List<CardListResponseDto> cardLists;

    public ListResponseDto(Board board) {
        this.id = board.getBoardId();
        this.boardTitle = board.getBoardTitle();
        this.boardDescription = board.getBoardDescription();
        this.cardLists = board.getCardLists().stream().map(CardListResponseDto::new).toList();
    }

}
