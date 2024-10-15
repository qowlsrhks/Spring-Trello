package com.sparta.springtrello.domain.search.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class BoardCardSearch {
    private String card_title;
    private String card_description;


    @Builder
    private BoardCardSearch(String card_title, String card_description) {
        this.card_title = card_title;
        this.card_description = card_description;
    }

    public static BoardCardSearch of(String card_title, String card_description) {
        return BoardCardSearch.builder().card_title(card_title).card_description(card_description).build();
    }
}
