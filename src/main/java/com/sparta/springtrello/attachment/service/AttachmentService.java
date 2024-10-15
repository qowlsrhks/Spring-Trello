package com.sparta.springtrello.attachment.service;

import com.sparta.springtrello.attachment.dto.response.UploadAttachment;
import com.sparta.springtrello.attachment.entity.Attachment;
import com.sparta.springtrello.attachment.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    @Value("${file.dir}")
    private String fileDir;

    private final AttachmentRepository attachmentRepository;

    public UploadAttachment uploadAttachment(Long cardId, MultipartFile file) {
        if(file.isEmpty()){

        }

        return null;
    }
}
