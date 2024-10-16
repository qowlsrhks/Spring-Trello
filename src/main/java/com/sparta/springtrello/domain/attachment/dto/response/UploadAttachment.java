package com.sparta.springtrello.domain.attachment.dto.response;


import com.sparta.springtrello.domain.attachment.entity.Attachment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadAttachment {

    private Long attachmentId;
    private Long cardId;
    private String fileName;
    private LocalDateTime createdAt;


    @Builder
    private UploadAttachment(Long attachmentId, Long cardId, String fileName,LocalDateTime createdAt) {
        this.attachmentId = attachmentId;
        this.cardId = cardId;
        this.fileName = fileName;
        this.createdAt = createdAt;
    }

    public static UploadAttachment uploadAttachment(Attachment attachment) {
        return UploadAttachment.builder()
                .attachmentId(attachment.getId())
                .fileName(attachment.getSaveFilename())
                .createdAt(attachment.getCreateAt())
                .build();
    }


    public static UploadAttachment testAttachment(Long attachmentId) {
        return UploadAttachment.builder()
                .attachmentId(attachmentId)
                .build();
    }

}
