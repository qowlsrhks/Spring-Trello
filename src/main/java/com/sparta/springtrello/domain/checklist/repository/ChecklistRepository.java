package com.sparta.springtrello.domain.checklist.repository;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.checklist.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByCardOrderByIdAsc(Card card);

}
