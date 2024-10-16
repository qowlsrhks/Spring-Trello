package com.sparta.springtrello.domain.search.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class BoardCardSearch {
    private String card_title;
    private Long cardList_id;
    private String card_description;


    @Builder
    private BoardCardSearch(String card_title, Long cardList_id, String card_description) {
        this.card_title = card_title;
        this.cardList_id = cardList_id;
        this.card_description = card_description;
    }

    public static BoardCardSearch of(String card_title, Long cardList_id, String card_description) {
        return BoardCardSearch.builder()
                .card_title(card_title)
                .cardList_id(cardList_id)
                .card_description(card_description)
                .build();
    }

    public static BoardCardSearch testBoardCard(String card_title, Long cardList_id, String card_description) {
        return BoardCardSearch.builder().card_title(card_title).cardList_id(cardList_id).card_description(card_description).build();
    }
}
