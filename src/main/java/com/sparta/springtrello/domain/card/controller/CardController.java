package com.sparta.springtrello.domain.card.controller;

import com.sparta.springtrello.domain.card.dto.CardArrangeRequestDto;
import com.sparta.springtrello.domain.card.dto.CardRequestDto;
import com.sparta.springtrello.domain.card.dto.CardResponseDto;
import com.sparta.springtrello.domain.card.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trello")
public class CardController {
    private final CardService cardService;

    @PostMapping("/list/{id}/card")
    public ResponseEntity<CardResponseDto> createCard(@PathVariable("id") Long id, @RequestBody CardRequestDto requestDto, HttpServletRequest httpServletRequest){
        String email = (String) httpServletRequest.getAttribute("email");
        CardResponseDto responseDto = cardService.createCard(id, requestDto, email);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/list/{listId}/card/{cardId}")
    public ResponseEntity<Long> deleteCard(@PathVariable("listId") Long listId, @PathVariable("cardId") Long cardId){
        Long id = cardService.deleteCard(listId, cardId);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/list/{id}/card")
    public ResponseEntity<List<CardResponseDto>> viewAllCardByListId(@PathVariable Long id){
        List<CardResponseDto> responseDtoList = cardService.viewAllCardByListId(id);
        return ResponseEntity.ok(responseDtoList);
    }

    @PatchMapping("/list/card")
    public ResponseEntity<Long> arrangeCard(@RequestBody CardArrangeRequestDto requestDto, HttpServletRequest httpServletRequest){
        String email = (String) httpServletRequest.getAttribute("email");
        Long id = cardService.arrangeCard(requestDto, email);
        return ResponseEntity.ok(id);
    }
}
