package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.board.dto.BoardResponseDto;
import com.sparta.springtrello.domain.board.dto.ListResponseDto;
import com.sparta.springtrello.domain.board.service.BoardService;
import com.sparta.springtrello.domain.common.Auth;
import com.sparta.springtrello.domain.common.AuthUser;
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
    @PostMapping("/{wsId}")
    public ResponseEntity<BoardResponseDto> createBoard(@Auth AuthUser authUser, @PathVariable("wsId") Long workSpaceId, @Valid @RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.createBoard(authUser,workSpaceId,boardRequestDto));
    }
//    단건조회
    @GetMapping("/{wsId}/{boardId}")
    public ResponseEntity<ListResponseDto> getBoard(@Auth AuthUser authUser, @PathVariable("wsId") Long workSpaceId, @PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(authUser,workSpaceId,boardId));
    }
//    다건조회
    @GetMapping("/{wsId}/boardList")
    public ResponseEntity<List<BoardResponseDto>> getBoards(@Auth AuthUser authUser,@PathVariable("wsId") Long workSpaceId) {
        return ResponseEntity.ok(boardService.getBoards(authUser,workSpaceId));
    }
//    수정
    @PutMapping("/{wsId}/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@Auth AuthUser authUser,@PathVariable("wsId") Long workSpaceId, @PathVariable("boardId") Long boardId,@Valid @RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.updateBoard(authUser,workSpaceId,boardId,boardRequestDto));
    }
//    삭제
    @DeleteMapping("/{wsId}/{boardId}")
    public ResponseEntity<String> deleteBoard(@Auth AuthUser authUser,@PathVariable("wsId") Long workSpaceId,@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(authUser,workSpaceId,boardId);
        return ResponseEntity.ok("삭제되었습니다.");
    }



}
