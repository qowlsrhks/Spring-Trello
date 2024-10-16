package com.sparta.springtrello.domain.attachment.service;

import com.sparta.springtrello.domain.advice.exception.UnAuthorizationException;
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
import com.sparta.springtrello.domain.user.entity.Role;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.MemberRole;
import com.sparta.springtrello.domain.workspace.entity.WorkSpace;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceMember;
import com.sparta.springtrello.domain.workspace.repository.WorkSpaceRepository;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceMemberRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private WorkSpaceRepository workSpaceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkspaceMemberRepository workspaceMemberRepository;

    @Mock
    private MultipartFile file;

    private final Long workSpaceId = 1L;
    private final Long cardId = 1L;

    @Nested
    class TestUploadAttachments {

        @Test
        public void testUploadAttachment_Success() throws IOException {

            AuthUser authUser = new AuthUser("asd", Role.ADMIN, 1L);

            Card card = new Card();
            card.setCardId(cardId);

            WorkspaceMember workspaceMember = new WorkspaceMember();
            workspaceMember.setRole(MemberRole.ADMIN);

            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(workSpaceRepository.findById(anyLong())).thenReturn(Optional.of(new WorkSpace()));
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
            when(workspaceMemberRepository.findByWorkSpaceAndUser(any(), any())).thenReturn(Optional.of(workspaceMember));

            when(file.isEmpty()).thenReturn(false);
            when(file.getOriginalFilename()).thenReturn("test_file.jpg");
            when(file.getSize()).thenReturn(1024L);

            Attachment savedAttachment = new Attachment();
            savedAttachment.setId(1L);
            when(attachmentRepository.save(any())).thenReturn(savedAttachment);

            UploadAttachment result = attachmentService.uploadAttachment(authUser, workSpaceId, cardId, file);

            assertNotNull(result);
            assertEquals(1L, result.getCardId());
        }

        @Test
        public void testUploadAttachment_WhenFileIsEmpty_ThrowsFileBadRequestException() {

            AuthUser authUser = new AuthUser("as", Role.ADMIN, 1L);

            when(file.isEmpty()).thenReturn(true);

            Exception exception = assertThrows(FileBadRequestException.class, () -> {
                attachmentService.uploadAttachment(authUser, workSpaceId, cardId, file);
            });

            assertEquals("업로드 할 파일이 없습니다.", exception.getMessage());
        }

        @Test
        public void testUploadAttachment_WhenCardNotFound_ThrowsCardNotFoundException() {

            AuthUser authUser = new AuthUser("as", Role.ADMIN, 1L);


            when(file.isEmpty()).thenReturn(false);
            when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

            Exception exception = assertThrows(CardNotFoundException.class, () -> {
                attachmentService.uploadAttachment(authUser, workSpaceId, cardId, file);
            });

            assertEquals("카드를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        public void testUploadAttachment_WhenUserRoleIsReadOnly_ThrowsUnAuthorizationException() {

            AuthUser authUser = new AuthUser("as", Role.ADMIN, 1L);


            Card card = new Card();
            card.setCardId(cardId);

            when(file.isEmpty()).thenReturn(false);
            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(workSpaceRepository.findById(anyLong())).thenReturn(Optional.of(new WorkSpace()));
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

            WorkspaceMember workspaceMember = new WorkspaceMember();
            workspaceMember.setRole(MemberRole.READ_ONLY);
            when(workspaceMemberRepository.findByWorkSpaceAndUser(any(), any())).thenReturn(Optional.of(workspaceMember));

            Exception exception = assertThrows(UnAuthorizationException.class, () -> {
                attachmentService.uploadAttachment(authUser, workSpaceId, cardId, file);
            });

            assertEquals("읽기 전용 유저는 첨부파일을 추가를 할 수 없습니다.", exception.getMessage());
        }

        @Test
        public void testUploadAttachment_WhenUnsupportedFileFormat_ThrowsFileBadRequestException() {
            AuthUser authUser = new AuthUser("as", Role.ADMIN, 1L);

            Card card = new Card();
            card.setCardId(cardId);

            when(file.isEmpty()).thenReturn(false);
            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(file.getOriginalFilename()).thenReturn("test_file.exe");
            when(workSpaceRepository.findById(anyLong())).thenReturn(Optional.of(new WorkSpace()));
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

            WorkspaceMember workspaceMember = new WorkspaceMember();
            workspaceMember.setRole(MemberRole.ADMIN);
            when(workspaceMemberRepository.findByWorkSpaceAndUser(any(), any())).thenReturn(Optional.of(workspaceMember));

            Exception exception = assertThrows(FileBadRequestException.class, () -> {
                attachmentService.uploadAttachment(authUser, workSpaceId, cardId, file);
            });
            assertEquals("지원되지 않는 파일 형식입니다.", exception.getMessage());
        }
    }


    @Nested
    class TestGetAttachment {

        @Test
        public void testGetAttachment_WhenCardNotFound_ThrowsCardNotFoundException() {
            when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

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
    class TestDownloadAttachment {

        @Test
        public void testDownloadAttachment_WhenCardNotFound_ThrowsCardNotFoundException() {
            when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

            Exception exception = assertThrows(CardNotFoundException.class, () ->
                    attachmentService.downloadAttachment(1L, 1L)
            );
            assertEquals("카드를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        public void testDownloadAttachment_WhenAttachmentNotFound_ThrowsAttachmentNotFoundException() {
            Card card = new Card();
            card.setCardId(1L);

            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(attachmentRepository.findByIdAndCard(anyLong(), any(Card.class))).thenReturn(Optional.empty());

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

            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(attachmentRepository.findByIdAndCard(anyLong(), any(Card.class))).thenReturn(Optional.of(attachment));

            Attachment result = attachmentService.downloadAttachment(1L, 1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("test_file.png", result.getSaveFilename());
        }
    }

    @Nested
    class TestDeleteAttachment {
        @Test
        public void testDeleteAttachment_Success() {
            Long attachmentId = 1L;
            AuthUser authUser = new AuthUser("asd", Role.ADMIN, 1L);

            Card card = new Card();
            card.setCardId(cardId);

            Attachment attachment = new Attachment();
            attachment.setIsDelete(AttachmentDeleteState.UNDELETED);

            WorkspaceMember workspaceMember = new WorkspaceMember();
            workspaceMember.setRole(MemberRole.ADMIN);

            when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
            when(attachmentRepository.findByIdAndCard(attachmentId, card)).thenReturn(Optional.of(attachment));
            when(workSpaceRepository.findById(workSpaceId)).thenReturn(Optional.of(new WorkSpace()));
            when(userRepository.findById(authUser.getUserId())).thenReturn(Optional.of(new User()));
            when(workspaceMemberRepository.findByWorkSpaceAndUser(any(), any())).thenReturn(Optional.of(workspaceMember));

            DeleteMessage result = attachmentService.deleteAttachment(authUser, workSpaceId, cardId, attachmentId);

            assertEquals("첨부파일이 삭제가 완료 되었습니다.", result.getMessage());
            assertEquals(AttachmentDeleteState.DELETED, attachment.getIsDelete());
        }

        @Test
        public void testDeleteAttachment_WhenCardNotFound_ThrowsCardNotFoundException() {
            Long attachmentId = 1L;
            AuthUser authUser = new AuthUser("asd", Role.ADMIN, 1L);

            when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

            Exception exception = assertThrows(CardNotFoundException.class, () -> {
                attachmentService.deleteAttachment(authUser, workSpaceId, cardId, attachmentId);
            });

            assertEquals("카드를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        public void testDeleteAttachment_WhenAttachmentNotFound_ThrowsAttachmentNotFoundException() {
            Long attachmentId = 1L;
            AuthUser authUser = new AuthUser("asd", Role.ADMIN, 1L);

            Card card = new Card();
            card.setCardId(cardId);

            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(attachmentRepository.findByIdAndCard(anyLong(), any(Card.class))).thenReturn(Optional.empty());

            Exception exception = assertThrows(AttachmentNotFoundException.class, () -> {
                attachmentService.deleteAttachment(authUser, workSpaceId, cardId, attachmentId);
            });

            assertEquals("해당 카드의 첨부파일이 없습니다.", exception.getMessage());
        }

        @Test
        public void testDeleteAttachment_WhenUserIsReadOnly_ThrowsUnAuthorizationException() {
            Long attachmentId = 1L;
            AuthUser authUser = new AuthUser("asd", Role.ADMIN, 1L);

            Card card = new Card();
            card.setCardId(cardId);

            WorkspaceMember workspaceMember = new WorkspaceMember();
            workspaceMember.setRole(MemberRole.READ_ONLY);

            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(workSpaceRepository.findById(anyLong())).thenReturn(Optional.of(new WorkSpace()));
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
            when(workspaceMemberRepository.findByWorkSpaceAndUser(any(), any())).thenReturn(Optional.of(workspaceMember));

            Exception exception = assertThrows(UnAuthorizationException.class, () -> {
                attachmentService.deleteAttachment(authUser, workSpaceId, cardId, attachmentId);
            });

            assertEquals("읽기 전용 유저는 첨부파일을 추가를 할 수 없습니다.", exception.getMessage());
        }

        @Test
        public void testDeleteAttachment_WhenDeletionFails_ThrowsAttachmentDataAccessException() {
            Long attachmentId = 1L;
            AuthUser authUser = new AuthUser("asd", Role.ADMIN, 1L);

            Card card = new Card();
            card.setCardId(cardId);

            Attachment attachment = new Attachment();
            attachment.setIsDelete(AttachmentDeleteState.UNDELETED);
            when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
            when(attachmentRepository.findByIdAndCard(anyLong(), any(Card.class))).thenReturn(Optional.of(attachment));

            WorkspaceMember workspaceMember = new WorkspaceMember();
            workspaceMember.setRole(MemberRole.ADMIN);
            when(workspaceMemberRepository.findByWorkSpaceAndUser(any(), any())).thenReturn(Optional.of(workspaceMember));

            Exception exception = assertThrows(AttachmentDataAccessException.class, () -> {
                attachmentService.deleteAttachment(authUser, workSpaceId, cardId, attachmentId);
            });

            assertEquals("해당 첨부파일이 삭제가 안 됐습니다.", exception.getMessage());
        }
    }


}