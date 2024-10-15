package com.sparta.springtrello.attachment.repository;

import com.sparta.springtrello.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByCardId(Long cardId);

    Optional<Attachment> findByIdAndCardId(Long attachmentId, Long cardId);
}
