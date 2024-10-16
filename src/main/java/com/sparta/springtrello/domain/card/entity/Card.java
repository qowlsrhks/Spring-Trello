package com.sparta.springtrello.domain.card.entity;

import com.sparta.springtrello.domain.attachment.entity.Attachment;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Card extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cardId;
    private String cardName;
    private String cardDescription;
    private LocalDateTime closingAt;
    private Long prevCardId;
    private Long nextCardId;

    @ManyToOne
    @JoinColumn(name = "listId")
    private CardList cardList;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<Attachment> attachments;
}
