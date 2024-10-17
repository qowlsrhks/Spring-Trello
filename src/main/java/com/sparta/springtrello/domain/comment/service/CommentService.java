package com.sparta.springtrello.domain.comment.service;

import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto createComment(Long cardId, String email, CommentRequestDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        String sanitizedContent = sanitizeContent(requestDto.getContents());
        Comment comment = new Comment(user, card, new CommentRequestDto(sanitizedContent));
        Comment saveComment = commentRepository.save(comment);
        card.increaseCommentCount();
        return new CommentResponseDto(saveComment);
    }


    public List<CommentResponseDto> getComments(Long cardId, String email) {
        userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        return commentRepository.findByCardOrderByModifiedAtDesc(card).stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, String email) {
        userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        String sanitizedContent = sanitizeContent(commentRequestDto.getContents());
        comment.update(new CommentRequestDto(sanitizedContent));
        return new CommentResponseDto(comment);
    }

    public CommentResponseDto deleteComment(Long cardId, Long commentId, String email) {
        userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        commentRepository.delete(comment);
        card.decreaseCommentCount();
        return new CommentResponseDto(comment);
    }

    private String sanitizeContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
        }

        // 이모지를 포함한 문자열 길이 체크
        if (content.length() > 1000) {
            throw new IllegalArgumentException("댓글은 1000자를 초과할 수 없습니다.");
        }

        // XSS 방지를 위한 HTML 이스케이프 처리
        return HtmlUtils.htmlEscape(content);
    }
}
