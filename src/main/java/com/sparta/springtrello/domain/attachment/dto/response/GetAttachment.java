package com.sparta.springtrello.domain.attachment.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class GetAttachment {
    private Long attachmentId;
    private String fileName;

    @Builder
    private GetAttachment(Long attachmentId, String fileName) {
        this.attachmentId = attachmentId;
        this.fileName = fileName;
    }

    public static GetAttachment of(Long attachmentId, String fileName) {
        return GetAttachment.builder()
                .attachmentId(attachmentId)
                .fileName(fileName)
                .build();
    }
}
