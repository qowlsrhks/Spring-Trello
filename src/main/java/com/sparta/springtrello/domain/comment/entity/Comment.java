package com.sparta.springtrello.domain.comment.entity;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springtrello.domain.common.Timestamped;
import com.sparta.springtrello.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    public Comment(User user, Card card, CommentRequestDto requestDto) {
        this.user = user;
        this.card = card;
        this.contents = requestDto.getContents();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.contents = commentRequestDto.getContents();
    }
}
