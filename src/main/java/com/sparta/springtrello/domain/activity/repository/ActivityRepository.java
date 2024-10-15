package com.sparta.springtrello.domain.activity.repository;

import com.sparta.springtrello.domain.activity.entity.Activity;
import com.sparta.springtrello.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByCardOrderByCreatedAtDesc(Card card);

}
