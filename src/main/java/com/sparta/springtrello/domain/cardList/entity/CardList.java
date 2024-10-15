package com.sparta.springtrello.domain.cardList.entity;

import com.sparta.springtrello.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "boardId", nullable = false)
//    private Board board;
}
