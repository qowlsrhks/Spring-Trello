package com.sparta.springtrello.domain.card.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardUpdateRequestDto {
    private List<Long> addUsers;
    private List<Long> removeUsers;
    private String cardName;
    private String cardDescription;
    private LocalDateTime closingAt;
}
