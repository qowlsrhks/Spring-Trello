package com.sparta.springtrello.domain.card.controller;

import com.sparta.springtrello.domain.card.dto.CardRequestDto;
import com.sparta.springtrello.domain.card.dto.CardResponseDto;
import com.sparta.springtrello.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trello")
public class CardController {
    private final CardService cardService;

    @PostMapping("/list/{id}/card")
    public ResponseEntity<CardResponseDto> createCard(@PathVariable("id") Long id, @RequestBody CardRequestDto requestDto){
        CardResponseDto responseDto = cardService.createCard(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
