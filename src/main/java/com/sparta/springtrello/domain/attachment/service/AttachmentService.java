package com.sparta.springtrello.domain.attachment.service;

import com.sparta.springtrello.domain.attachment.dto.response.DeleteMessage;
import com.sparta.springtrello.domain.attachment.dto.response.GetAttachment;
import com.sparta.springtrello.domain.attachment.dto.response.UploadAttachment;
import com.sparta.springtrello.domain.attachment.entity.Attachment;
import com.sparta.springtrello.domain.attachment.entity.AttachmentDeleteState;
import com.sparta.springtrello.domain.attachment.exception.AttachmentDataAccessException;
import com.sparta.springtrello.domain.attachment.exception.AttachmentNotFoundException;
import com.sparta.springtrello.domain.attachment.exception.FileBadRequestException;
import com.sparta.springtrello.domain.attachment.repository.AttachmentRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.exception.CardNotFoundException;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.common.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AttachmentService {

    @Value("${file.dir}")
    private String fileDir;

    private final AttachmentRepository attachmentRepository;
    private final CardRepository cardRepository;

    @Transactional
    public UploadAttachment uploadAttachment(AuthUser authUser,Long wordSpaceId, Long cardId, MultipartFile file) throws IOException {
        if(file.isEmpty()){
            log.error("File is empty");
            throw new FileBadRequestException("업로드 할 파일이 없습니다.");
        }

        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("카드를 찾을 수 없습니다.")
        );

        //유저 역할 읽기 전용 예외처리

        String fileName = file.getOriginalFilename();
        log.info("originalFilename : {}", fileName);

        String uuid = UUID.randomUUID().toString();
        log.info("uuid : {}", uuid);

        String ext = fileName.substring(fileName.lastIndexOf("."));
        log.info("ext : {}", ext);

        if(!(ext.equals(".jpg") || ext.equals(".png") || ext.equals(".pdf") || ext.equals(".csv"))){
            log.error("FileBadRequestException {}", ext);
            throw new FileBadRequestException("지원되지 않는 파일 형식입니다.");
        }

        String saveName = uuid + ext;
        log.info("save name : {}", saveName);

        String filePath = fileDir + saveName;
        log.info("file path : {}", filePath);

        Long fileSize = file.getSize();
        log.info("fileSize : {}", fileSize);

        Attachment attachment = Attachment.uploadAttachment(fileName, saveName, filePath,fileSize,card);

        file.transferTo(new File(filePath));

        Attachment savedAttachment =  attachmentRepository.save(attachment);


        return UploadAttachment.uploadAttachment(savedAttachment);
    }

    public List<GetAttachment> getAttachment(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("카드를 찾을 수 없습니다.")
        );
        List<Attachment> attachments = attachmentRepository.findByCard(card);
        if (attachments.isEmpty()) {
            log.error("Attachment is empty");
            throw new AttachmentNotFoundException("해당 카드의 첨부파일이 없습니다.");
        }

        return attachments.stream()
                .map(attachment -> GetAttachment.of(attachment.getId(), attachment.getSaveFilename())).toList();

    }


    public Attachment downloadAttachment(Long cardId, Long attachmentId) {
        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("카드를 찾을 수 없습니다.")
        );
        return attachmentRepository.findByIdAndCard(attachmentId, card).orElseThrow(
                () -> new AttachmentNotFoundException("해당 카드의 첨부파일이 없습니다.")
        );
    }

    @Transactional
    public DeleteMessage deleteAttachment(AuthUser authUser, Long workSpaceId, Long cardId, Long attachmentId) {

        //유저 역할 읽기 전용 예외처리

        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("카드를 찾을 수 없습니다.")
        );

        Attachment attachment = attachmentRepository.findByIdAndCard(attachmentId, card).orElseThrow(
                () -> new AttachmentNotFoundException("해당 카드의 첨부파일이 없습니다.")
        );


        attachment.deleteAttachment(LocalDateTime.now(), AttachmentDeleteState.DELETED);

        if(attachment.getIsDelete().equals(AttachmentDeleteState.UNDELETED)){
            throw new AttachmentDataAccessException("해당 첨부파일이 삭제가 안 됐습니다.");
        }

        return DeleteMessage.of("첨부파일이 삭제가 완료 되었습니다.");
    }
}
