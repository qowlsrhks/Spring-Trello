package com.sparta.springtrello.domain.attachment.service;

import com.sparta.springtrello.domain.attachment.dto.response.GetAttachment;
import com.sparta.springtrello.domain.attachment.entity.Attachment;
import com.sparta.springtrello.domain.attachment.exception.AttachmentNotFoundException;
import com.sparta.springtrello.domain.attachment.exception.FileBadRequestException;
import com.sparta.springtrello.domain.attachment.repository.AttachmentRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.exception.CardNotFoundException;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.common.AuthUser;
import com.sparta.springtrello.domain.user.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private AttachmentRepository attachmentRepository;
    @InjectMocks
    private AttachmentService attachmentService;

    @Mock
    private MultipartFile file;

    private final Long wordSpaceId = 1L;
    private final Long cardId = 1L;

    @Nested
    class TestUpload {

        @Test
        void testUploadAttachment_FileIsEmpty() {
            AuthUser authUser = new AuthUser("email", Role.USER, 1L);
            when(file.isEmpty()).thenReturn(true);

            FileBadRequestException exception = assertThrows(FileBadRequestException.class, () ->
                    attachmentService.uploadAttachment(authUser, wordSpaceId, cardId, file)
            );

            assertEquals("업로드 할 파일이 없습니다.", exception.getMessage());
        }

        @Test
        void testUploadAttachment_CardNotFound() {
            AuthUser authUser = new AuthUser("email", Role.USER, 1L);
            when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

            CardNotFoundException exception = assertThrows(CardNotFoundException.class, () ->
                    attachmentService.uploadAttachment(authUser, wordSpaceId, cardId, file)
            );

            assertEquals("카드를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        void testUploadAttachment_UnsupportedFileType() {
            AuthUser authUser = new AuthUser("email", Role.USER, 1L);
            when(file.isEmpty()).thenReturn(false);
            when(file.getOriginalFilename()).thenReturn("test.exe");
            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(new Card()));

            FileBadRequestException exception = assertThrows(FileBadRequestException.class, () ->
                    attachmentService.uploadAttachment(authUser, wordSpaceId, cardId, file)
            );

            assertEquals("지원되지 않는 파일 형식입니다.", exception.getMessage());
        }
    }

    @Nested
    class TestGetAttachment {

        @Test
        public void testGetAttachment_WhenCardNotFound_ThrowsCardNotFoundException() {
            when(cardRepository.findById(1L)).thenReturn(Optional.empty());

            CardNotFoundException exception = assertThrows(CardNotFoundException.class, () ->
                    attachmentService.getAttachment(1L)
            );

            assertEquals("카드를 찾을 수 없습니다.", exception.getMessage());
        }


        @Test
        public void testGetAttachment_WhenAttachmentsEmpty_ThrowsAttachmentNotFoundException() {
            Card card = new Card();
            card.setCardId(1L);

            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(attachmentRepository.findByCard(any(Card.class))).thenReturn(Collections.emptyList());

            AttachmentNotFoundException exception = assertThrows(AttachmentNotFoundException.class, () ->
                    attachmentService.getAttachment(1L)
            );

            assertEquals("해당 카드의 첨부파일이 없습니다.", exception.getMessage());
        }

        @Test
        public void testGetAttachment_ReturnsAttachmentList() {
            Card card = new Card();
            card.setCardId(1L);
            card.setCardName("asd");

            Attachment attachment = new Attachment();
            attachment.setId(1L);
            attachment.setSaveFilename("test_file.png");

            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(attachmentRepository.findByCard(any(Card.class))).thenReturn(List.of(attachment));

            List<GetAttachment> result = attachmentService.getAttachment(card.getCardId());

            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getAttachmentId());
            assertEquals("test_file.png", result.get(0).getFileName());
        }
    }


    @Nested
    class TestDownload {

        @Test
        public void testDownloadAttachment_WhenCardNotFound_ThrowsCardNotFoundException() {
            when(cardRepository.findById(1L)).thenReturn(Optional.empty());

            Exception exception = assertThrows(CardNotFoundException.class, () ->
                    attachmentService.downloadAttachment(1L, 1L)
            );

            assertEquals("카드를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        public void testDownloadAttachment_WhenAttachmentNotFound_ThrowsAttachmentNotFoundException() {
            Card card = new Card();
            card.setCardId(1L);

            when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
            when(attachmentRepository.findByIdAndCard(1L, card)).thenReturn(Optional.empty());

            Exception exception = assertThrows(AttachmentNotFoundException.class, () ->
                    attachmentService.downloadAttachment(1L, 1L)
            );

            assertEquals("해당 카드의 첨부파일이 없습니다.", exception.getMessage());
        }

        @Test
        public void testDownloadAttachment_ReturnsAttachment() {
            Card card = new Card();
            card.setCardId(1L);
            Attachment attachment = new Attachment();
            attachment.setId(1L);
            attachment.setSaveFilename("test_file.png");

            when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
            when(attachmentRepository.findByIdAndCard(1L, card)).thenReturn(Optional.of(attachment));

            Attachment result = attachmentService.downloadAttachment(1L, 1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("test_file.png", result.getSaveFilename());
        }
    }


}