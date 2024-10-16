package com.sparta.springtrello.domain.activity.entity;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.checklist.entity.Checklist;
import com.sparta.springtrello.domain.checklist.entity.ChecklistItem;
import com.sparta.springtrello.domain.comment.entity.Comment;
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
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_item_id")
    private ChecklistItem checklistItem;

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

    public Activity(Checklist checklist, User user, ActivityType activityType, String details) {
        this.checklist = checklist;
        this.user = user;
        this.type = activityType;
        this.contents = details;
    }

    public Activity(ChecklistItem checklistItem, User user, ActivityType activityType, String details) {
        this.checklistItem = checklistItem;
        this.user = user;
        this.type = activityType;
        this.contents = details;
    }

    public Activity(ChecklistItem checklistItem, User user, ActivityType activityType) {
        this.checklistItem = checklistItem;
        this.user = user;
        this.type = activityType;
    }
}
