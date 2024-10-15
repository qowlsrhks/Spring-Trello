package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.board.service.BoardService;
import lombok.NoArgsConstructor;
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
    @PostMapping("/{wsId}")
    public ResponseEntity<BoardResponseDto> createBoard(@PathVariable("wsId") Long workSpaceId, @RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.createBoard(workSpaceId,boardRequestDto));
    }
//    단건조회
    @GetMapping("/{wsId}/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable("wsId") Long workSpaceId, @PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(workSpaceId,boardId));
    }
//    다건조회
    @GetMapping("/{wsId}/boardList")
    public ResponseEntity<List<BoardResponseDto>> getBoards(@PathVariable("wsId") Long workSpaceId) {
        return ResponseEntity.ok(boardService.getBoards(workSpaceId));
    }
//    수정
    @PutMapping("/{wsId}/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable("wsId") Long workSpaceId, @PathVariable("boardId") Long boardId, @RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.updateBoard(workSpaceId,boardId,boardRequestDto));
    }
//    삭제
    @DeleteMapping("/{wsId}/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("wsId") Long workSpaceId,@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(workSpaceId,boardId);
        return ResponseEntity.ok("삭제되었습니다.");
    }



}
