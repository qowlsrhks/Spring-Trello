package com.sparta.springtrello.search.controller;

import com.sparta.springtrello.search.dto.response.CardSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {


    @GetMapping("/cards/search")
    public ResponseEntity<CardSearch> searchCard() {

    }


}
