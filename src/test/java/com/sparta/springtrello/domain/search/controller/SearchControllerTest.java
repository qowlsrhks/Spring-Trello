package com.sparta.springtrello.domain.search.controller;

import com.sparta.springtrello.domain.card.controller.CardController;
import com.sparta.springtrello.domain.search.dto.response.BoardCardSearch;
import com.sparta.springtrello.domain.search.dto.response.CardSearch;
import com.sparta.springtrello.domain.search.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = SearchController.class)
class SearchControllerTest {

    @InjectMocks
    private CardController cardController;

    @Mock
    private SearchService searchService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }


    @Test
    public void testSearchCard_Success() throws Exception {
        String cardName = "Test Card";
        int page = 0;
        int size = 10;

        List<CardSearch> cardSearchList = new ArrayList<>();
        CardSearch cardSearch = CardSearch.testCardSearch("Test Card", "qwe");
        cardSearchList.add(cardSearch);

        when(searchService.searchCard(cardName, page, size)).thenReturn(cardSearchList);

        mockMvc.perform(get("/cards/search")
                        .param("card-name", cardName)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Card"));
    }

    @Test
    public void testSearchCard_NoResults() throws Exception {
        String cardName = "Nonexistent Card";
        int page = 0;
        int size = 10;

        List<CardSearch> cardSearchList = new ArrayList<>();

        when(searchService.searchCard(anyString(), anyInt(), anyInt())).thenReturn(cardSearchList);

        mockMvc.perform(get("/cards/search")
                        .param("card-name", cardName)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testSearchBoardCard_Success() throws Exception {
        Long boardId = 1L;

        List<BoardCardSearch> cardList = new ArrayList<>();
        BoardCardSearch boardCardSearch = BoardCardSearch.testBoardCard("Test Card",1L, "asd");
        cardList.add(boardCardSearch);

        List<List<BoardCardSearch>> boardCardSearchList = new ArrayList<>();
        boardCardSearchList.add(cardList);

        when(searchService.boardCardSearch(anyLong())).thenReturn(boardCardSearchList);

        mockMvc.perform(get("/boards/{boardId}/cards/search", boardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0][0].id").value(1L))
                .andExpect(jsonPath("$[0][0].title").value("Test Card"));
    }

    @Test
    public void testSearchBoardCard_NoResults() throws Exception {
        Long boardId = 1L;

        List<List<BoardCardSearch>> boardCardSearchList = new ArrayList<>();

        when(searchService.boardCardSearch(anyLong())).thenReturn(boardCardSearchList);

        mockMvc.perform(get("/boards/{boardId}/cards/search", boardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }



}