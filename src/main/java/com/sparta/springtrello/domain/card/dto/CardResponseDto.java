package com.sparta.springtrello.domain.card.dto;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
        this.prevCardId = card.getPrevCardId();
        this.nextCardId = card.getNextCardId();
        this.listId = card.getCardList().getListId();
        this.check = card.isChecked();
        this.users = card.getUsers();
        this.archived = card.isArchived();
    }

    private Long cardId;
    private String cardName;
    private String cardDescription;
    private LocalDateTime closingAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long prevCardId;
    private Long nextCardId;
    private Long listId;
    private boolean check;
    private List<User> users;
    private boolean archived;
    private int commentCount;

}
