package com.sparta.springtrello.domain.card.entity;

import com.sparta.springtrello.domain.attachment.entity.Attachment;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.common.Timestamped;
import com.sparta.springtrello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "cards",
        indexes = {
                @Index(name = "idx_card_name", columnList = "card_name"),
                @Index(name = "idx_closing_at", columnList = "closing_at")
        })
public class Card extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cardId;
    private String cardName;
    private String cardDescription;
    private LocalDateTime closingAt;
    private Long prevCardId;
    private Long nextCardId;
    private boolean checked = false;
    // 체크박스 만들기

    @ManyToOne
    @JoinColumn(name = "listId")
    private CardList cardList;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<Attachment> attachments;

    @ManyToMany
    @JoinTable(
            name = "card_user", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "card_id"), // Card 엔티티와 연결된 FK
            inverseJoinColumns = @JoinColumn(name = "user_id") // User 엔티티와 연결된 FK
    )
    private List<User> users;



}
