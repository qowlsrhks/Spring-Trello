package com.sparta.springtrello.attachment.controller;

import com.sparta.springtrello.attachment.dto.response.UploadAttachment;
import com.sparta.springtrello.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/{cardId}/attachments")
    public ResponseEntity<UploadAttachment> createAttchment(@PathVariable("cardId") String cardId, @RequestParam("file") MultipartFile file) {
        UploadAttachment uploadAttachment = attachmentService.uploadAttachment(cardId, file);
        return ResponseEntity.ok().body(uploadAttachment);
    }
}
