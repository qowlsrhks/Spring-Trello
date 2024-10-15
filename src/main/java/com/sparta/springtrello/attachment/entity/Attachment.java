package com.sparta.springtrello.attachment.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Attachment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "origin_filename")
    private String originalFilename;

    @Column(nullable = false, name = "save_filename")
    private String saveFilename;

    @Column(nullable = false, name = "file_path")
    private String filePath;

    @Column(nullable = false, name = "file_size")
    private Long fileSize;

    @Column(nullable = false, name = "is_delete")
    @Enumerated(EnumType.STRING)
    private AttachmentDeleteState isDelete;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = true)
    private Card card;


    @Builder
    private Attachment(String originalFilename, String saveFilename, String filePath, Long fileSize, AttachmentDeleteState isDelete, LocalDateTime createAt, LocalDateTime deleteAt
    , Card card) {
        this.originalFilename = originalFilename;
        this.saveFilename = saveFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.isDelete = isDelete;
        this.createAt = createAt;
        this.deleteAt = deleteAt;
        this.card = card;
    }


    public static Attachment uploadAttachment(String originalFilename, String saveFilename, String filePath, Long fileSize, Card card) {
        return Attachment.builder()
                .originalFilename(originalFilename)
                .saveFilename(saveFilename)
                .filePath(filePath)
                .fileSize(fileSize)
                .isDelete(AttachmentDeleteState.UNDELETED)
                .createAt(LocalDateTime.now())
                .deleteAt(null)
                .card(card)
                .build();
    }
}
