package com.sparta.springtrello.attachment.service;

import com.sparta.springtrello.attachment.dto.response.DeleteMessage;
import com.sparta.springtrello.attachment.dto.response.GetAttachment;
import com.sparta.springtrello.attachment.dto.response.UploadAttachment;
import com.sparta.springtrello.attachment.entity.Attachment;
import com.sparta.springtrello.attachment.exception.AttachmentDataAccessException;
import com.sparta.springtrello.attachment.exception.AttachmentNotFoundException;
import com.sparta.springtrello.attachment.exception.FileBadRequestException;
import com.sparta.springtrello.attachment.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @Transactional
    public UploadAttachment uploadAttachment(Long cardId, MultipartFile file) throws IOException {
        if(file.isEmpty()){
            log.error("File is empty");
            throw new FileBadRequestException("업로드 할 파일이 없습니다.");
        }

        //읽기 전용 역할을 가진 유저 예외처리 추가
        //cardId 써서 첨부파일 DB에 넣을 card 조회 후 추가

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

        Attachment attachment = Attachment.uploadAttachment(fileName, saveName, filePath,fileSize/*카드 엔티티 추가 후 주석 제거,null*/);

        file.transferTo(new File(filePath));

        Attachment savedAttachment =  attachmentRepository.save(attachment);


        return UploadAttachment.uploadAttachment(savedAttachment);
    }

    public List<GetAttachment> getAttachment(Long cardId) {
        List<Attachment> attachments = attachmentRepository.findByCardId(cardId);
        if (attachments.isEmpty()) {
            log.error("Attachment is empty");
            throw new AttachmentNotFoundException("해당 카드의 첨부파일이 없습니다.");
        }

        return attachments.stream()
                .map(attachment -> GetAttachment.of(attachment.getId(), attachment.getSaveFilename())).toList();

    }


    public Attachment downloadAttachment(Long cardId, Long attachmentId) {
        return attachmentRepository.findByIdAndCardId(attachmentId, cardId).orElseThrow(
                () -> new AttachmentNotFoundException("해당 카드의 첨부파일이 없습니다.")
        );
    }

    @Transactional
    public DeleteMessage deleteAttachment(Long cardId, Long attachmentId) {

        //유저 역할 읽기 전용 예외처리


        Attachment attachment = attachmentRepository.findByIdAndCardId(attachmentId, cardId).orElseThrow(
                () -> new AttachmentNotFoundException("해당 카드의 첨부파일이 없습니다.")
        );

        try{
            attachmentRepository.delete(attachment);
        } catch (DataAccessException e){
            throw new AttachmentDataAccessException("첨부파일 삭제 중 오류가 발생했습니다.");
        }

        return DeleteMessage.of("첨부파일이 삭제가 완료 되었습니다.");
    }
}
