package com.sparta.springtrello.domain.notification.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    private static final String SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/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";

    public void sendSlackNotification(String message) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonPayload = "{ \"text\": \"" + message + "\" }";

        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
        ResponseEntity<String> response = restTemplate.exchange(SLACK_WEBHOOK_URL, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Message sent to Slack successfully");
        } else {
            System.out.println("Failed to send message to Slack: " + response.getStatusCode());
        }
    }
}
