package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.service.BoardService;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@NoArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private BoardService boardService;

    //    생성
    @PostMapping("/")
    public ResponseEntity<BoardResponseDto> createBoard(BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.createBoard(boardRequestDto));
    }
//    단건조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }
//    다건조회
    @GetMapping("/")
    public ResponseEntity<List<BoardResponseDto>> getBoards() {
        return ResponseEntity.ok(boardService.getBoards());
    }
//    수정
    @PutMapping("/")
    public ResponseEntity<BoardResponseDto> updateBoard(BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.updateBoard(boardRequestDto));
    }
//    삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("삭제되었습니다.");
    }



}
