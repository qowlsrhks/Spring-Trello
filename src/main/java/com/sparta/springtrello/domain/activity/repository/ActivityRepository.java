package com.sparta.springtrello.domain.activity.repository;

import com.sparta.springtrello.domain.activity.entity.Activity;
import com.sparta.springtrello.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("select c from Activity c join fetch c.user where c.card = :card")
    List<Activity> findByCardOrderByCreatedAtDesc(@Param("card") Card card);

}
