package com.sparta.springtrello.domain.activity.entity;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.common.Timestamped;
import com.sparta.springtrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
