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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CardListRepository cardListRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    public void testSearchCard_WhenNoCardsFound_ThrowsCardNotFoundException() {
        String cardName = "Nonexistent Card";

        when(cardRepository.findByCardName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        Exception exception = assertThrows(CardNotFoundException.class, () ->
            searchService.searchCard(cardName, 0, 10)
        );

        assertEquals("카드를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    public void testSearchCard_ReturnsCardSearchList() {
        String cardName = "Test Card";

        Card card = new Card();
        card.setCardName(cardName);
        card.setCardDescription("Test Description");
        card.setClosingAt(LocalDateTime.of(2024,1,1,1,1));

        List<Card> cards = List.of(card);
        Page<Card> cardPage = new PageImpl<>(cards);

        when(cardRepository.findByCardName(anyString(), any(Pageable.class))).thenReturn(cardPage);

        List<CardSearch> result = searchService.searchCard(cardName, 0, 10);

        assertEquals(1, result.size());
        assertEquals("Test Card", result.get(0).getCard_title());
        assertEquals("Test Description", result.get(0).getCard_description());
        assertEquals(LocalDateTime.of(2024,1,1,1,1), result.get(0).getClosing_at());
    }

    @Test
    public void testBoardCardSearch_WhenBoardNotFound_ThrowsBoardNotFoundException() {
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class, () ->
            searchService.boardCardSearch(1L)
        );

        assertEquals("보드를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    public void testBoardCardSearch_WhenCardListNotFound_ThrowsCardListNotFoundException() {
        Board board = new Board();
        board.setBoardId(1L);

        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));
        when(cardListRepository.findByBoard(any(Board.class))).thenReturn(Collections.emptyList());

        CardListNotFoundException exception = assertThrows(CardListNotFoundException.class, () ->
            searchService.boardCardSearch(1L)
        );

        assertEquals("리스트를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    public void testBoardCardSearch_WhenCardsNotFound_ThrowsCardNotFoundException() {
        Board board = new Board();
        board.setBoardId(1L);
        CardList cardList = new CardList();
        cardList.setListId(1L);
        cardList.setCards(Collections.emptyList());

        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));
        when(cardListRepository.findByBoard(any(Board.class))).thenReturn(List.of(cardList));

        CardNotFoundException exception = assertThrows(CardNotFoundException.class, () ->
           searchService.boardCardSearch(1L)
        );

        assertEquals("카드를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    public void testBoardCardSearch_ReturnsBoardCardSearchList() {
        Board board = new Board();
        board.setBoardId(1L);
        CardList cardList = new CardList();
        cardList.setListId(1L);

        Card card = new Card();
        card.setCardName("Test Card");
        card.setCardDescription("Test Description");

        cardList.setCards(List.of(card));

        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));
        when(cardListRepository.findByBoard(any(Board.class))).thenReturn(List.of(cardList));

        List<List<BoardCardSearch>> result = searchService.boardCardSearch(1L);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals("Test Card", result.get(0).get(0).getCard_title());
        assertEquals(1L, result.get(0).get(0).getCardList_id());
        assertEquals("Test Description", result.get(0).get(0).getCard_description());
    }

}