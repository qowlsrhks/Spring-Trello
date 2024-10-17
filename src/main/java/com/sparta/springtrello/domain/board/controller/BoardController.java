package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.dto.ListResponseDto;
import com.sparta.springtrello.domain.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    //    생성
    @PostMapping("/{userId}/{wsId}")
    public ResponseEntity<BoardResponseDto> createBoard(@PathVariable("userId") Long userId, @PathVariable("wsId") Long workSpaceId, @Valid @RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.createBoard(userId,workSpaceId,boardRequestDto));
    }
//    단건조회
    @GetMapping("/{userId}/{wsId}/{boardId}")
    public ResponseEntity<ListResponseDto> getBoard(@PathVariable("userId") Long userId, @PathVariable("wsId") Long workSpaceId, @PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(userId,workSpaceId,boardId));
    }
//    다건조회
    @GetMapping("/{userId}/{wsId}/boardList")
    public ResponseEntity<List<BoardResponseDto>> getBoards(@PathVariable("userId") Long userId,@PathVariable("wsId") Long workSpaceId) {
        return ResponseEntity.ok(boardService.getBoards(userId,workSpaceId));
    }
//    수정
    @PutMapping("/{userId}/{wsId}/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable("userId") Long userId,@PathVariable("wsId") Long workSpaceId, @PathVariable("boardId") Long boardId,@Valid @RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.updateBoard(userId,workSpaceId,boardId,boardRequestDto));
    }
//    삭제
    @DeleteMapping("/{userId}/{wsId}/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("userId") Long userId,@PathVariable("wsId") Long workSpaceId,@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(userId,workSpaceId,boardId);
        return ResponseEntity.ok("삭제되었습니다.");
    }



}
