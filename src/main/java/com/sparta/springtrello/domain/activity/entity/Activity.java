package com.sparta.springtrello.domain.activity.entity;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.common.Timestamped;
import com.sparta.springtrello.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "activities")
@Getter
@NoArgsConstructor
public class Activity extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;


    public Activity(Card card, User user, ActivityType type, String details, Object o) {
        this.card = card;
        this.user = user;
        this.type = type;
        this.contents = details;
    }

}
