package com.sparta.springtrello.domain.cardList.repository;

import com.sparta.springtrello.domain.board.entity.Board;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardListRepository extends JpaRepository<CardList, Long> {

    @Query("select c from CardList c join fetch c.cards where c.board = :board")
    List<CardList> findByBoard(@Param("board") Board board);

    List<CardList> findAllByBoard(Long id);
}
