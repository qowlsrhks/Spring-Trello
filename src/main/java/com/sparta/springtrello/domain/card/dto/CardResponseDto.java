package com.sparta.springtrello.domain.card.dto;

import com.sparta.springtrello.domain.card.entity.Card;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardResponseDto {
    public CardResponseDto(Card card) {
        this.cardId = card.getCardId();
        this.cardName = card.getCardName();
        this.cardDescription = card.getCardDescription();
        this.closingAt = card.getClosingAt();
        this.createdAt = card.getCreatedAt();
        this.modifiedAt = card.getModifiedAt();
        this.listId = card.getCardList().getListId();
    }

    private Long cardId;
    private String cardName;
    private String cardDescription;
    private LocalDateTime closingAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long listId;
}
