package com.sparta.springtrello.domain.search.service;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.exception.BoardNotFoundException;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.exception.CardNotFoundException;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.cardList.exception.CardListNotFoundException;
import com.sparta.springtrello.domain.cardList.repository.CardListRepository;
import com.sparta.springtrello.domain.search.dto.response.BoardCardSearch;
import com.sparta.springtrello.domain.search.dto.response.CardSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CardRepository cardRepository;
    private final BoardRepository boardRepository;
    private final CardListRepository cardListRepository;


    public List<CardSearch> searchCard(String cardName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Card> cardPage = cardRepository.findByCardNameContains(cardName,pageable);

        if(cardPage.isEmpty()) {
            throw new CardNotFoundException("카드를 찾을 수 없습니다.");
        }

        return cardPage.stream().map(card ->
                CardSearch.of(card.getCardName(), card.getCardDescription(), card.getClosingAt())
        ).toList();
    }

    public List<List<BoardCardSearch>> boardCardSearch(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("보드를 찾을 수 없습니다.")
        );

        List<CardList> cardList = cardListRepository.findByBoard(board);

        if(cardList.isEmpty()) {
            throw new CardListNotFoundException("리스트를 찾을 수 없습니다.");
        }

        List<List<BoardCardSearch>> boardCardSearch = new ArrayList<>();
        for(CardList cardList1 : cardList) {
            List<Card> cards = cardList1.getCards();
            if(cards.isEmpty()) {
                throw new CardNotFoundException("카드를 찾을 수 없습니다.");
            }

            List<BoardCardSearch> boardCardSearch1 = cards.stream().map(card ->
                    BoardCardSearch.of(card.getCardName(),cardList1.getListId(), card.getCardDescription())).toList();

            boardCardSearch.add(boardCardSearch1);
        }

        return boardCardSearch;
    }
}
