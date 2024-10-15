package com.sparta.springtrello.domain.card.dto;

import lombok.Getter;

@Getter
public class CardArrangeRequestDto {
    private Long toListId;
    private Long cardId;
    private Long prevCardId;
}
