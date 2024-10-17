package com.sparta.springtrello.domain.attachment.repository;

import com.sparta.springtrello.domain.attachment.entity.Attachment;
import com.sparta.springtrello.domain.attachment.entity.AttachmentDeleteState;
import com.sparta.springtrello.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByCardAndIsDelete(Card card, AttachmentDeleteState state);

    Optional<Attachment> findByIdAndCardAndIsDelete(Long attachmentId, Card card, AttachmentDeleteState deleteState);
}
