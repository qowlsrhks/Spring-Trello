package com.sparta.springtrello.domain.comment.activity.repository;

import com.sparta.springtrello.domain.comment.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.smartcardio.Card;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByCardOrderByCreatedAtDesc(Card card);

}
