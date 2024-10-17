package com.sparta.springtrello.domain.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.board.dto.ListResponseDto;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.common.Timestamped;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "board")
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "board_title" ,nullable = false)
    private String boardTitle;

    @Column(name = "board_description", nullable = false)
    private String boardDescription;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workspace_id", nullable = false)
    @JsonBackReference
    private WorkSpace workSpace;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<CardList> cardLists;


    public Board(BoardRequestDto boardRequestDto) {
        this.boardTitle = boardRequestDto.getBoardTitle();
        this.boardDescription = boardRequestDto.getBoardDescription();
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.boardTitle = boardRequestDto.getBoardTitle();
        this.boardDescription = boardRequestDto.getBoardDescription();
    }

}

