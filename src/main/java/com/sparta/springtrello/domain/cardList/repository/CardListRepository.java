package com.sparta.springtrello.domain.cardList.repository;

import com.sparta.springtrello.domain.cardList.entity.CardList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CardListRepository extends JpaRepository<CardList, Long> {

    List<CardList> findAllByListId(Long id);

    Optional<CardList> findByPrevListIdIsNull();
}
