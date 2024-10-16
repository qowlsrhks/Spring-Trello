package com.sparta.springtrello.domain.cardList.entity;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CardList extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long listId;
    private String listName;
    private Long prevListId;
    private Long nextListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "cardList", fetch = FetchType.LAZY)
    private List<Card> cards;
}
