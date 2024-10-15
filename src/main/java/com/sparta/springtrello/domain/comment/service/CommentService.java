package com.sparta.springtrello.domain.comment.service;

import com.sparta.springtrello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.user.entity.User;
import com.sparta.springtrello.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.smartcardio.Card;
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
        Card card = cardRepository.findByid(cardId).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        Comment comment = commentRepository.save(user, card, requestDto);
        return new CommentResponseDto(comment);
    }


    public List<CommentResponseDto> getComments(Long cardId, String email) {
        userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        Card card = findById(cardId.orElseThrows(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        return commentRepository.findByCardOrderByModifiedAtDesc(card).stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, String email) {
        userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.update(commentRequestDto);
        return new CommentResponseDto(comment);
    }

    public CommentResponseDto deleteComment(Long commentId, String email) {
        userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
        return new CommentResponseDto(comment);
    }
}
