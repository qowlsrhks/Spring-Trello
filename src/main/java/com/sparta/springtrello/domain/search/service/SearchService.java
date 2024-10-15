package com.sparta.springtrello.domain.search.service;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.exception.CardNotFoundException;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.search.dto.response.BoardCardSearch;
import com.sparta.springtrello.domain.search.dto.response.CardSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CardRepository cardRepository;


    public List<CardSearch> searchCard(String cardName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Card> cardPage = cardRepository.findByCardName(cardName,pageable);

        if(cardPage.isEmpty()) {
            throw new CardNotFoundException("카드를 찾을 수 없습니다.");
        }

        return cardPage.stream().map(card ->
                CardSearch.of(card.getCardName(), card.getCardDescription(), card.getClosingAt())
        ).toList();
    }

    public BoardCardSearch boardCardSearch(Long boardId) {
        return new BoardCardSearch();
    }
}
