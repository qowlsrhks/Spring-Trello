package com.sparta.springtrello.domain.cardList.dto;

import com.sparta.springtrello.domain.cardList.entity.CardList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardListResponseDto {
    public CardListResponseDto(CardList cardList) {
        this.id = cardList.getListId();
        this.listName = cardList.getListName();
        this.prevListId = cardList.getPrevListId();
        this.nextListId = cardList.getNextListId();
    }
    private Long id;
    private String listName;
    private Long prevListId;
    private Long nextListId;
}
