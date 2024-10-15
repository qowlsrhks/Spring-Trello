package com.sparta.springtrello.domain.card.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardRequestDto {
    private String cardName;
    private String cardDescription;
    private LocalDateTime closingAt;
}
