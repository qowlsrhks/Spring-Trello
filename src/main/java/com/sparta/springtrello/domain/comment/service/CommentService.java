package com.sparta.springtrello.domain.comment.service;

import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springtrello.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.workspace.entity.MemberRole;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceMember;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Transactional
    public CommentResponseDto createComment(Long cardId, String email, CommentRequestDto requestDto) {
        User loggedInUser = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("권한이 없습니다."));
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new IllegalArgumentException("카드를 찾을 수 없습니다."));
        List<WorkspaceMember> workspaceMember = workspaceMemberRepository.findByUserEmail(loggedInUser.getEmail());
        if(workspaceMember.isEmpty()) {
            throw new IllegalArgumentException("역할이 없습니다.");
        }
        List<WorkspaceMember> getWorkspaceMemberRole = workspaceMemberRepository.findByRole(workspaceMember.get(0).getRole());

        if(getWorkspaceMemberRole.get(0).getRole() == MemberRole.READ_ONLY) {
            throw new IllegalArgumentException("읽기 전용 멤버입니다.");
        }
        Comment comment = new Comment(loggedInUser, card, requestDto);
        Comment saveDomment = commentRepository.save(comment);
        card.increaseCommentCount();
        return new CommentResponseDto(saveDomment);
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
        comment.update(commentRequestDto);
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
}
