package com.sparta.springtrello.domain.attachment.controller;

import com.sparta.springtrello.domain.attachment.dto.response.DeleteMessage;
import com.sparta.springtrello.domain.attachment.dto.response.GetAttachment;
import com.sparta.springtrello.domain.attachment.dto.response.UploadAttachment;
import com.sparta.springtrello.domain.attachment.entity.Attachment;
import com.sparta.springtrello.domain.attachment.exception.AttachmentNotFoundException;
import com.sparta.springtrello.domain.attachment.exception.FileBadRequestException;
import com.sparta.springtrello.domain.attachment.service.AttachmentService;
import com.sparta.springtrello.domain.common.AuthUser;
import com.sparta.springtrello.domain.user.entity.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AttachmentController.class)
class AttachmentControllerTest {

    @InjectMocks
    private AttachmentController attachmentController;

    @Mock
    private AttachmentService attachmentService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class TestGetAttachment {

        @Test
        public void testGetAttachment_Success() throws Exception {
            mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();

            Long cardId = 1L;
            GetAttachment attachment = GetAttachment.of(1L, "test_file.png");

            List<GetAttachment> attachments = List.of(attachment);

            when(attachmentService.getAttachment(anyLong())).thenReturn(attachments);

            mockMvc.perform(get("/cards/{cardId}/attachments", cardId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].saveFilename").value("test_file.png"));
        }

        @Test
        public void testGetAttachment_WhenNoAttachmentsFound_ThrowsAttachmentNotFoundException() throws Exception {
            mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();

            Long cardId = 1L;

            when(attachmentService.getAttachment(anyLong())).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/cards/{cardId}/attachments", cardId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect((ResultMatcher) content().string("[]"));
        }
    }

    @Nested
    class TestUpdateAttachment {
        @Test
        public void testUploadAttachment_Success() throws Exception {
            MockMultipartFile file = new MockMultipartFile("file", "test_file.jpg", MediaType.IMAGE_JPEG_VALUE, "test content".getBytes());
            UploadAttachment uploadAttachment = UploadAttachment.testAttachment(1L);

            when(attachmentService.uploadAttachment(any(), anyLong(), anyLong(), any())).thenReturn(uploadAttachment);

            mockMvc.perform(multipart("/cards/{cardId}/attachments/workspaces/{wsId}", 1L, 1L)
                            .file(file)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .sessionAttr("authUser", new AuthUser("asd", Role.ADMIN,1L)))
                    .andExpect(status().isOk())
                    .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        public void testUploadAttachment_WhenIOExceptionThrown() throws Exception {
            MockMultipartFile file = new MockMultipartFile("file", "test_file.jpg", MediaType.IMAGE_JPEG_VALUE, "test content".getBytes());

            when(attachmentService.uploadAttachment(any(), anyLong(), anyLong(), any())).thenThrow(new IOException("File upload error"));

            // 실행 및 검증
            mockMvc.perform(multipart("/cards/{cardId}/attachments/workspaces/{wsId}", 1L, 1L)
                            .file(file)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .sessionAttr("authUser", new AuthUser("asd", Role.ADMIN,1L)))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        public void testUploadAttachment_WhenFileIsEmpty_ThrowsFileBadRequestException() throws Exception {
            MockMultipartFile file = new MockMultipartFile("file", "", MediaType.TEXT_PLAIN_VALUE, new byte[0]);

            when(attachmentService.uploadAttachment(any(), anyLong(), anyLong(), any())).thenThrow(new FileBadRequestException("업로드 할 파일이 없습니다."));

            mockMvc.perform(multipart("/cards/{cardId}/attachments/workspaces/{wsId}", 1L, 1L)
                            .file(file)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .sessionAttr("authUser", new AuthUser("asd", Role.ADMIN,1L)))
                    .andExpect(status().isBadRequest()) // 예외 발생 시 400 상태 코드 확인
                    .andExpect((ResultMatcher) content().string("업로드 할 파일이 없습니다."));
        }
    }


    @Nested
    class TestDownloadAttachment{
        @Test
        public void testDownloadAttachmentFile_Success() throws Exception {
            Long cardId = 1L;
            Long attachmentId = 1L;
            String filePath = "/path/to/file/test_file.jpg";
            String originalFileName = "test_file.jpg";

            Attachment attachment = new Attachment();
            attachment.setFilePath(filePath);
            attachment.setOriginalFilename(originalFileName);

            when(attachmentService.downloadAttachment(anyLong(), anyLong())).thenReturn(attachment);

            UrlResource resource = new UrlResource("file:" + filePath);
            when(resource.exists()).thenReturn(true);

            mockMvc.perform(get("/cards/{cardId}/attachments/{attachmentId}/download", cardId, attachmentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\""))
                    .andExpect((ResultMatcher) content().contentType(MediaType.IMAGE_JPEG));
        }

        @Test
        public void testDownloadAttachmentFile_WhenMalformedURLExceptionThrown() throws Exception {
            Long cardId = 1L;
            Long attachmentId = 1L;

            when(attachmentService.downloadAttachment(anyLong(), anyLong())).thenThrow(new MalformedURLException("Invalid file URL"));

            mockMvc.perform(get("/cards/{cardId}/attachments/{attachmentId}/download", cardId, attachmentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class TestDeleteAttachment {
        @Test
        public void testDeleteAttachment_Success() throws Exception {
            Long workSpaceId = 1L;
            Long attachmentId = 1L;
            DeleteMessage deleteMessage = DeleteMessage.of("삭제되었습니다");

            when(attachmentService.deleteAttachment(any(), anyLong(), anyLong(), anyLong())).thenReturn(deleteMessage);

            // 실행 및 검증
            mockMvc.perform(put("/attachments/{attachmentId}/workspaces/{wsId}", attachmentId, workSpaceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .sessionAttr("authUser", new AuthUser("asd",Role.ADMIN,1L)))
                    .andExpect(status().isOk())
                    .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("삭제되었습니다."));
        }

        @Test
        public void testDeleteAttachment_WhenAttachmentNotFound_ThrowsAttachmentNotFoundException() throws Exception {
            Long workSpaceId = 1L;
            Long attachmentId = 1L;

            when(attachmentService.deleteAttachment(any(), anyLong(), anyLong(), anyLong())).thenThrow(new AttachmentNotFoundException("첨부파일을 찾을 수 없습니다."));

            // 실행 및 검증
            mockMvc.perform(put("/attachments/{attachmentId}/workspaces/{wsId}", attachmentId, workSpaceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .sessionAttr("authUser", new AuthUser("asd",Role.ADMIN,1L)))
                    .andExpect(status().isNotFound()) // 예외 발생 시 404 상태 코드 확인
                    .andExpect((ResultMatcher) content().string("첨부파일을 찾을 수 없습니다."));
        }
    }


}