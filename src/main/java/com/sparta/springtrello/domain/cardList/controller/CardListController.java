package com.sparta.springtrello.domain.cardList.controller;

import com.sparta.springtrello.domain.cardList.dto.CardListArrangeRequestDto;
import com.sparta.springtrello.domain.cardList.dto.CardListRequestDto;
import com.sparta.springtrello.domain.cardList.dto.CardListResponseDto;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.cardList.service.CardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trello")
public class CardListController {
    private final CardListService listService;

    @PostMapping("/list")
    ResponseEntity<CardListResponseDto> createList(@RequestBody CardListRequestDto requestDto) {
        CardListResponseDto responseDto = listService.createList(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/list/{id}")
    ResponseEntity<Long> deleteList(@PathVariable Long id) {
        Long deletedId = listService.deleteList(id);
        return ResponseEntity.ok(deletedId);
    }

    @GetMapping("/list/{id}")
    ResponseEntity<List<CardListResponseDto>> getAllLists(@PathVariable Long id) {
        List<CardListResponseDto> responseDtos = listService.viewAllListSortedByLinked(id);
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/list/{id}")
    ResponseEntity<CardListResponseDto> updateList(@PathVariable Long id,
                                                   @RequestBody CardListRequestDto requestDto) {
        CardListResponseDto responseDto = listService.updateList(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/list")
    ResponseEntity<Long> arrangeList(@RequestBody CardListArrangeRequestDto requestDto) {
        Long arrangedId = listService.arrangeList(requestDto);
        return ResponseEntity.ok(arrangedId);
    }
}
