package com.sparta.springtrello.domain.search.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CardSearch {
    private String card_title;
    private String card_description;
    private LocalDateTime closing_at;
//    private String username;


    @Builder
    private CardSearch(String card_title, String card_description, LocalDateTime closing_at) {
        this.card_title = card_title;
        this.card_description = card_description;
        this.closing_at = closing_at;
//        this.username = username;
    }

    public static CardSearch of(String card_title, String card_description, LocalDateTime closing_at) {
        return CardSearch.builder()
                .card_title(card_title)
                .card_description(card_description)
                .closing_at(closing_at)
//                .username(username)
                .build();
    }
}
