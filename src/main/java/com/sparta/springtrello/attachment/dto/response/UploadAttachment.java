package com.sparta.springtrello.attachment.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadAttachment {

    private Long attachmentId;
    private Long cardId;
    private String fileName;
    private String fileType;
    private LocalDateTime createdAt;


    @Builder
    private UploadAttachment(Long attachmentId, Long cardId, String fileName, String fileType, LocalDateTime createdAt) {
        this.attachmentId = attachmentId;
        this.cardId = cardId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.createdAt = createdAt;
    }

    public static UploadAttachment uploadAttachment() {
        return UploadAttachment.builder().build();
    }

}
