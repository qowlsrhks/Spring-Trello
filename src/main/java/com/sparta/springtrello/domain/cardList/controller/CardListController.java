package com.sparta.springtrello.domain.cardList.controller;

import com.sparta.springtrello.domain.cardList.dto.CardListArrangeRequestDto;
import com.sparta.springtrello.domain.cardList.dto.CardListRequestDto;
import com.sparta.springtrello.domain.cardList.dto.CardListResponseDto;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.cardList.service.CardListService;
import com.sparta.springtrello.domain.common.Auth;
import com.sparta.springtrello.domain.common.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trello")
public class CardListController {
    private final CardListService listService;

    @PostMapping("/list/{id}")
    ResponseEntity<CardListResponseDto> createList(@RequestBody CardListRequestDto requestDto, @Auth AuthUser authUser, @PathVariable Long id) {
        CardListResponseDto responseDto = listService.createList(requestDto, authUser, id);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/list/{id}")
    ResponseEntity<Long> deleteList(@Auth AuthUser authUser ,@PathVariable Long id) {
        Long deletedId = listService.deleteList(authUser,id);
        return ResponseEntity.ok(deletedId);
    }

    @GetMapping("/list/{id}")
    ResponseEntity<List<CardListResponseDto>> getAllLists(@Auth AuthUser authUser,@PathVariable Long id) {
        List<CardListResponseDto> responseDtos = listService.viewAllListSortedByLinked(authUser,id);
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/list/{id}")
    ResponseEntity<CardListResponseDto> updateList(@Auth AuthUser authUser,
                                                   @PathVariable Long id,
                                                   @RequestBody CardListRequestDto requestDto) {
        CardListResponseDto responseDto = listService.updateList(authUser,id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/list")
    ResponseEntity<Long> arrangeList(@Auth AuthUser authUser,@RequestBody CardListArrangeRequestDto requestDto) {
        Long arrangedId = listService.arrangeList(requestDto);
        return ResponseEntity.ok(arrangedId);
    }
}
