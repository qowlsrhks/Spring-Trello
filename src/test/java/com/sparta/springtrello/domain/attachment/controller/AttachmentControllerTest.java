package com.sparta.springtrello.domain.attachment.controller;

import com.sparta.springtrello.domain.attachment.dto.response.GetAttachment;
import com.sparta.springtrello.domain.attachment.entity.Attachment;
import com.sparta.springtrello.domain.attachment.service.AttachmentService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    class TestDownloadAttachment {

        @Test
        public void testDownloadAttachmentFile_Success() throws Exception {
            mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();

            Long cardId = 1L;
            Long attachmentId = 1L;
            String filePath = "/path/to/file/test_file.png";
            String originalFileName = "test_file.png";

            Attachment attachment = new Attachment();
            attachment.setFilePath(filePath);
            attachment.setOriginalFilename(originalFileName);


            when(attachmentService.downloadAttachment(anyLong(), anyLong())).thenReturn(attachment);

            UrlResource resource = new UrlResource("file:" + filePath);
            when(attachmentService.downloadAttachment(anyLong(), anyLong())).thenReturn(attachment);

            mockMvc.perform(get("/cards/{cardId}/attachments/{attachmentId}/download", cardId, attachmentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\""))
                    .andExpect((ResultMatcher) content().contentType(getMediaType(originalFileName)));
        }

        @Test
        public void testDownloadAttachmentFile_WhenMalformedURLExceptionThrown() throws Exception {
            mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();

            Long cardId = 1L;
            Long attachmentId = 1L;

            when(attachmentService.downloadAttachment(anyLong(), anyLong())).thenThrow(new MalformedURLException("Invalid file URL"));

            mockMvc.perform(get("/cards/{cardId}/attachments/{attachmentId}/download", cardId, attachmentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }

        private MediaType getMediaType(String filename) {
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (filename.endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (filename.endsWith(".pdf")) {
                mediaType = MediaType.APPLICATION_PDF;
            } else if (filename.endsWith(".csv")) {
                mediaType = MediaType.TEXT_PLAIN;
            }
            return mediaType;
        }
    }


}