package com.sparta.springtrello.domain.notification.controller;

import com.sparta.springtrello.domain.notification.service.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

@RestController
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/sse/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamEvents() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(sequence -> {
                    String message = "Notification at " + LocalTime.now();
                    // Slack으로 알림을 보내는 메서드 호출
                    notificationService.sendSlackNotification("New SSE Event: " + message);
                    return message;
                });
    }
}
