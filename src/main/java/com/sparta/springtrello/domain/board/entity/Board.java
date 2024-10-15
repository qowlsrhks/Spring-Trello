package com.sparta.springtrello.domain.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.springtrello.domain.board.dto.BoardRequestDto;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "board_title" ,nullable = false)
    private String boardTitle;

    @Column(name = "board_description", nullable = false)
    private String boardDescription;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workspace_id", nullable = false)
    @JsonBackReference
    private WorkSpace workSpace;

    public Board(BoardRequestDto boardRequestDto) {

        this.boardTitle = boardRequestDto.getBoardTitle();
        this.boardDescription = boardRequestDto.getBoardDescription();
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public void update(String boardTitle, String boardDescription, LocalDateTime modifiedAt) {
        this.boardTitle = boardTitle;
        this.boardDescription = boardDescription;
        this.modifiedAt = modifiedAt;
    }

}
