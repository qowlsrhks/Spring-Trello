package com.sparta.springtrello.domain.card.repository;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByCardList(CardList cardList);
}
