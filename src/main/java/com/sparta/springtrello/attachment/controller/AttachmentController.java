package com.sparta.springtrello.attachment.controller;

import com.sparta.springtrello.attachment.dto.response.DeleteMessage;
import com.sparta.springtrello.attachment.dto.response.GetAttachment;
import com.sparta.springtrello.attachment.dto.response.UploadAttachment;
import com.sparta.springtrello.attachment.entity.Attachment;
import com.sparta.springtrello.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards/{cardId}/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<UploadAttachment> uploadAttachment(@PathVariable Long cardId, @RequestPart("file") MultipartFile file) throws IOException {
        UploadAttachment uploadAttachment = attachmentService.uploadAttachment(cardId, file);
        return ResponseEntity.ok().header(String.valueOf(HttpStatus.OK)).body(uploadAttachment);
    }

    @GetMapping
    public ResponseEntity<List<GetAttachment>> getAttachment(@PathVariable Long cardId) {
        List<GetAttachment> getAttachment = attachmentService.getAttachment(cardId);
        return ResponseEntity.ok().body(getAttachment);
    }

    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> downloadAttachmentFile(@PathVariable Long cardId, @PathVariable Long attachmentId) throws MalformedURLException {
        Attachment attachment = attachmentService.downloadAttachment(cardId, attachmentId);
        UrlResource resource = new UrlResource("file:" + attachment.getFilePath());
        String originalFileName = attachment.getOriginalFilename();

        String contentDisposition = "attachment; filename=\"" + originalFileName + "\"";

        MediaType mediaType = getMediaType(originalFileName);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)
                .contentType(mediaType)
                .body(resource);
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<DeleteMessage> deleteAttachment(@PathVariable Long cardId, @PathVariable Long attachmentId) {
        DeleteMessage deleteMessage = attachmentService.deleteAttachment(cardId, attachmentId);
        return ResponseEntity.ok().header(String.valueOf(HttpStatus.OK)).body(deleteMessage);
    }

    private static MediaType getMediaType(String originalFileName) {
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        if(originalFileName.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        }else if(originalFileName.endsWith(".jpg") || originalFileName.endsWith(".jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        }else if(originalFileName.endsWith(".pdf")) {
            mediaType = MediaType.APPLICATION_PDF;
        }else if(originalFileName.endsWith(".csv")){
            mediaType = MediaType.TEXT_PLAIN;
        }
        return mediaType;
    }
}
