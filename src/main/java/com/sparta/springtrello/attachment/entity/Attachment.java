package com.sparta.springtrello.attachment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Attachment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "origin_filename")
    private String originalFilename;

    @Column(nullable = false, name = "save_filename")
    private String saveFilename;

    @Column(nullable = false, name = "file_size")
    private Long fileSize;

    @Column(nullable = false, name = "is_delete")
    private AttachmentDeleteState isDelete;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "card_id", nullable = false)
//    @JsonBackReference
//    private Card card;
}
