package com.sparta.springtrello.domain.search.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardSearch {
    private String card_title;
    private String card_description;
    private LocalDateTime closing_at;


    @Builder
    private CardSearch(String card_title, String card_description, LocalDateTime closing_at) {
        this.card_title = card_title;
        this.card_description = card_description;
        this.closing_at = closing_at;
    }

    public static CardSearch of(String card_title, String card_description, LocalDateTime closing_at) {
        return CardSearch.builder()
                .card_title(card_title)
                .card_description(card_description)
                .closing_at(closing_at)
                .build();
    }


    public static CardSearch testCardSearch(String card_title, String card_description) {
        return CardSearch.builder().card_title(card_title).card_description(card_description).build();
    }
}
