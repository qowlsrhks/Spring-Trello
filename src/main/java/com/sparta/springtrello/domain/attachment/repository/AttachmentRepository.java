package com.sparta.springtrello.domain.attachment.repository;

import com.sparta.springtrello.domain.attachment.entity.Attachment;
import com.sparta.springtrello.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByCard(Card card);

    Optional<Attachment> findByIdAndCard(Long attachmentId, Card card);
}
