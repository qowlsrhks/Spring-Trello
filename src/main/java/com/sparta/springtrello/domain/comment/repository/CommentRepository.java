package com.sparta.springtrello.domain.comment.repository;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join fetch c.user where c.card = :card")
    List<Comment> findByCardOrderByModifiedAtDesc(@Param("card") Card card);
}
