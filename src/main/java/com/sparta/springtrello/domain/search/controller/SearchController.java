package com.sparta.springtrello.domain.search.controller;

import com.sparta.springtrello.domain.search.dto.response.BoardCardSearch;
import com.sparta.springtrello.domain.search.dto.response.CardSearch;
import com.sparta.springtrello.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/cards/search")
    public ResponseEntity<List<CardSearch>> searchCard(@RequestParam("card-name") String cardName,
                                                       @RequestParam(defaultValue = "0", value = "page") int page,
                                                       @RequestParam(defaultValue = "10", value = "size") int size) {
        List<CardSearch> cardSearch = searchService.searchCard(cardName,page,size);
        return ResponseEntity.ok().body(cardSearch);
    }


    @GetMapping("/boards/{boardId}/cards/search")
    public ResponseEntity<BoardCardSearch> searchBoardCard(@PathVariable Long boardId) {
        BoardCardSearch boardCardSearch = searchService.boardCardSearch(boardId);
        return ResponseEntity.ok().body(boardCardSearch);
    }


}
