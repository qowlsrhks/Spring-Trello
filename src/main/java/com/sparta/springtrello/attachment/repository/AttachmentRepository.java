package com.sparta.springtrello.attachment.repository;

import com.sparta.springtrello.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
