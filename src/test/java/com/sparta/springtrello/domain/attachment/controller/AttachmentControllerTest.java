package com.sparta.springtrello.domain.attachment.controller;

import com.sparta.springtrello.domain.attachment.dto.response.GetAttachment;
import com.sparta.springtrello.domain.attachment.service.AttachmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AttachmentController.class)
class AttachmentControllerTest {

    @InjectMocks
    private AttachmentController attachmentController;

    @Mock
    private AttachmentService attachmentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAttachment_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();

        Long cardId = 1L;
        GetAttachment attachment = GetAttachment.of(1L, "test_file.png");

        List<GetAttachment> attachments = List.of(attachment);

        when(attachmentService.getAttachment(cardId)).thenReturn(attachments);

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

        when(attachmentService.getAttachment(cardId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cards/{cardId}/attachments", cardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) content().string("[]")); // 빈 리스트 확인
    }





}