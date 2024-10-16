package com.sparta.springtrello.domain.comment.controller;

import com.sparta.springtrello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springtrello.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("comments/{cardId}")
    ResponseEntity<CommentResponseDto> createComment(@PathVariable Long cardId, @RequestBody CommentRequestDto requestDto, HttpServletRequest httpServletRequest) {
        String email = (String) httpServletRequest.getAttribute("email");
        return ResponseEntity.ok(commentService.createComment(cardId, email, requestDto));
    }

    @GetMapping("comments/{cardId}")
    ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long cardId, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(commentService.getComments(cardId, email));
    }

    @PutMapping("comments/{commentId}")
    ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        String email = (String) httpServletRequest.getAttribute("email");
        return ResponseEntity.ok(commentService.updateComment(commentId, commentRequestDto, email));
    }

    @DeleteMapping("comments/{commentId}")
    ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(commentService.deleteComment(commentId, email));
    }
}